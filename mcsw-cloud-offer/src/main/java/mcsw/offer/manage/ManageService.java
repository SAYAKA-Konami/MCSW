package mcsw.offer.manage;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.http.HttpStatus;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import mcsw.offer.client.UserClient;
import mcsw.offer.entity.CivilServant;
import mcsw.offer.entity.GetId;
import mcsw.offer.entity.Master;
import mcsw.offer.entity.Offer;
import mcsw.offer.model.dto.QueryOfferDto;
import mcsw.offer.model.dto.RequestShowOffDto;
import mcsw.offer.model.vo.CommentVo;
import mcsw.offer.model.vo.MixBrowserVo;
import mcsw.offer.model.vo.MixFlauntVo;
import mcsw.offer.model.vo.UserOfFlauntVo;
import mcsw.offer.service.CivilServantService;
import mcsw.offer.service.CommentService;
import mcsw.offer.service.MasterService;
import mcsw.offer.service.OfferService;
import mcsw.offer.service.extend.browser.GetMixBrowser;
import mcsw.offer.service.extend.startegy.flaunt.FlauntStrategy;
import mcsw.offer.service.extend.startegy.getInfo.GetInfoStrategy;
import mscw.common.aop.UserMajor;
import mscw.common.api.CommonResult;
import mscw.common.api.ResultCode;
import mscw.common.domain.dto.RequestPage;
import mscw.common.domain.vo.UserVO;
import mscw.common.exception.ApiException;
import mscw.common.util.JsonUtil;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.CompletableFuture;

import static mcsw.offer.config.Constant.WATCH_COUNT_KEY;
import static mscw.common.config.Constants.SERVER_ERROR;

@Service
@Slf4j
public class ManageService {
    @Resource
    private Map<Integer, FlauntStrategy> flauntStrategyMap;

    @Resource
    private Map<Integer, GetInfoStrategy> getInfoStrategyMap;

    @Resource
    private Map<Integer, GetMixBrowser> getMixBrowserMap;

    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @Resource
    private ThreadPoolTaskExecutor offerTaskExecutor;
    @Resource
    private OfferService offerService;
    @Resource
    private MasterService masterService;
    @Resource
    private CivilServantService civilServantService;
    @Resource
    private CommentService commentService;
    @Autowired
    private UserClient userClient;

    /**
     *  晒出offer或上岸记录
     * @param header 包含发布者的基础信息
     */
    @UserMajor
    public CommonResult<String> flaunt(Map<String, String> header, RequestShowOffDto requestShowOffDto){
        FlauntStrategy flauntStrategy = flauntStrategyMap.get(requestShowOffDto.getCategory());
        return flauntStrategy.flaunt(header, requestShowOffDto);
    }

    /**
     *  获取offer或上岸记录的详情
     */
    public CommonResult<MixFlauntVo> getInfoOfOne(QueryOfferDto queryOfferDto){
        GetInfoStrategy getInfoStrategy = getInfoStrategyMap.get(queryOfferDto.getCategory());
        Optional<MixFlauntVo> infoOptional = getInfoStrategy.getInfo(queryOfferDto.getId());
        MixFlauntVo data = null;
        if (infoOptional.isPresent()) {
            // 填充用户信息
            data = infoOptional.get();
            Integer userId = data.getUserId();
            CommonResult<List<UserVO>> userByIds = userClient.getUserByIds(ListUtil.of(userId));
            if (userByIds.getCode() != HttpStatus.HTTP_OK) {
                return CommonResult.failed(userByIds.getMessage());
            }
            UserVO userVO = userByIds.getData().get(0);
            UserOfFlauntVo userOfFlauntVo = new UserOfFlauntVo();
            BeanUtils.copyProperties(userVO, userOfFlauntVo);
            data.setUserOfFlauntVo(userOfFlauntVo);
            List<CommentVo> comments = commentService.getCommentFacade(queryOfferDto);
            data.setCommentVoList(comments);
            // 异步执行
            offerTaskExecutor.execute(() -> {
                // 每点击一次就增加一次访问量。左为帖子ID，右为帖子种类
                Pair<Integer, Integer> id_category = new ImmutablePair<>(queryOfferDto.getId(), queryOfferDto.getCategory());
                try {
                    String value = JsonUtil.toJson(id_category);
                    stringRedisTemplate.opsForZSet().addIfAbsent(WATCH_COUNT_KEY,  value, 0);
                    stringRedisTemplate.opsForZSet().incrementScore(WATCH_COUNT_KEY, value, 1.0);
                } catch (Exception e) {
                    log.error("序列化失败", e);
                }
            });
        }
        return data == null ? CommonResult.failed(SERVER_ERROR) : CommonResult.success(data);
    }

    /**
     *  获取综合排行榜
     */
    public CommonResult<IPage<MixBrowserVo>> getMixData(RequestPage requestPage){
        Long current = requestPage.getCurrent();
        Long size = requestPage.getSize();
        long begin = (current - 1) * size;
        long end = begin + size;
        Set<String> range = stringRedisTemplate.opsForZSet().range(WATCH_COUNT_KEY, begin, end);
        List<Pair<Integer, Integer>> sortedList = new ArrayList<>();
        List<Integer> offerIds = new ArrayList<>();
        List<Integer> masterIds = new ArrayList<>();
        List<Integer> csIds = new ArrayList<>();
        for (String json : range) {
            try {
                Pair<Integer, Integer> id_category = JsonUtil.fromJson(json, Pair.class);
                switch (id_category.getRight()) {
                    case 0 :
                        offerIds.add(id_category.getLeft());
                        sortedList.add(id_category);
                        break;
                    case 1 :
                        masterIds.add(id_category.getLeft());
                        sortedList.add(id_category);
                        break;
                    case 2 :
                        csIds.add(id_category.getLeft());
                        sortedList.add(id_category);
                        break;
                    default:
                        throw new ApiException(ResultCode.FAILED);
                }
            } catch (Exception e) {
                log.error("反序列化失败", e);
            }
        }
        // 由于这里数据量不大，所以直接串行处理
        List<Offer> offers = offerService.listByIds(offerIds);
        List<Master> masters = masterService.listByIds(masterIds);
        List<CivilServant> civilServants = civilServantService.listByIds(csIds);
        // 组装map以便重新排序
        Map<Integer, Map<Integer, MixBrowserVo>> map = new HashMap<>();
        getCategory2Map(offerIds, FlauntStrategy.Category.OFFER, offers, map);
        getCategory2Map(masterIds, FlauntStrategy.Category.MASTER, masters, map);
        getCategory2Map(csIds, FlauntStrategy.Category.CIVIL_SERVANT, civilServants, map);
        List<MixBrowserVo> result = new ArrayList<>(requestPage.getSize().intValue());
        for (Pair<Integer, Integer> pair : sortedList) {
            MixBrowserVo mixBrowserVo = map.get(pair.getRight()).get(pair.getLeft());
            result.add(mixBrowserVo);
        }
        Page<MixBrowserVo> page = Page.of(requestPage.getCurrent(), requestPage.getSize());
        page.setRecords(result);
        return CommonResult.success(page);
    }

    /**
     *  为什么要填充传入的map？因为分别查询的时候打乱了redis中的排序结果。所以这里需要将组装得到的vo类组装成一个map。最后再进行重新排序。
     * @param ids 对应帖子的ID
     * @param category 类别
     * @param entities 由ids查出的实体类集合
     * @param map key：int value of category，value: (map key - id value: result)
     */
    private void getCategory2Map(List<Integer> ids, FlauntStrategy.Category category, List entities
            , Map<Integer, Map<Integer, MixBrowserVo>> map){
        GetMixBrowser getMixBrowser = getMixBrowserMap.get(category.getTag());
        Map<Integer, MixBrowserVo> MixBrowserVoMap = getMixBrowser.get(entities, ids);
        map.put(category.getTag(), MixBrowserVoMap);
    }



}
