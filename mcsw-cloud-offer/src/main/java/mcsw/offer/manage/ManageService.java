package mcsw.offer.manage;

import cn.hutool.http.HttpStatus;
import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.extern.slf4j.Slf4j;
import mcsw.offer.client.UserClient;
import mcsw.offer.entity.CivilServant;
import mcsw.offer.entity.Master;
import mcsw.offer.entity.Offer;
import mcsw.offer.model.dto.QueryOfferDto;
import mcsw.offer.model.dto.RequestShowOffDto;
import mcsw.offer.model.vo.MixBrowserVo;
import mcsw.offer.model.vo.MixFlauntVo;
import mcsw.offer.service.CivilServantService;
import mcsw.offer.service.MasterService;
import mcsw.offer.service.OfferService;
import mcsw.offer.service.extend.browser.GetMixBrowser;
import mcsw.offer.service.extend.startegy.flaunt.FlauntStrategy;
import mcsw.offer.service.extend.startegy.getInfo.GetInfoStrategy;
import mscw.common.aop.UserMajor;
import mscw.common.api.CommonResult;
import mscw.common.api.ResultCode;
import mscw.common.domain.dto.RequestPage;
import mscw.common.exception.ApiException;
import mscw.common.util.JsonUtil;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

import static mcsw.offer.config.Constant.WATCH_COUNT_KEY;

@Service
@Slf4j
public class ManageService {
    @Resource
    private Map<Integer, FlauntStrategy> flauntStrategyMap;

    @Resource
    private Map<Integer, GetInfoStrategy> getInfoStrategyMap;

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
     *  TODO: 拼装回复内容
     */
    public CommonResult<MixFlauntVo> getInfoOfOne(QueryOfferDto queryOfferDto){
        GetInfoStrategy getInfoStrategy = getInfoStrategyMap.get(queryOfferDto.getCategory());
        CommonResult<MixFlauntVo> info = getInfoStrategy.getInfo(queryOfferDto.getId());
        // 每点击一次就增加一次访问量
        if (info.getCode() == HttpStatus.HTTP_OK) {
            // 左为帖子ID，右为帖子种类
            Pair<Integer, Integer> id_category = new ImmutablePair<>(queryOfferDto.getId(), queryOfferDto.getCategory());
            try {
                String value = JsonUtil.toJson(id_category);
                stringRedisTemplate.opsForZSet().addIfAbsent(WATCH_COUNT_KEY,  value, 0);
                stringRedisTemplate.opsForZSet().incrementScore(WATCH_COUNT_KEY, value, 1.0);
            } catch (Exception e) {
                log.error("序列化失败", e);
            }
        }
        return info;
    }

    /**
     *  获取综合排行榜
     *  TODO: 待完善...
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
        GetMixBrowser<Offer, OfferService> getMixBrowser = new GetMixBrowser<>(userClient, offerTaskExecutor, stringRedisTemplate, offerService);
        List<Master> masters = masterService.listByIds(masterIds);
        List<CivilServant> civilServants = civilServantService.listByIds(csIds);

        return null;
    }



}
