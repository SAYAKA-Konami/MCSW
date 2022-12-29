package mcsw.offer.service;

import cn.hutool.core.date.DateTime;
import cn.hutool.http.HttpStatus;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import mcsw.offer.client.UserClient;
import mcsw.offer.dao.OfferDao;
import mcsw.offer.entity.Offer;
import mcsw.offer.model.vo.MixBrowserVo;
import mcsw.offer.service.extend.startegy.flaunt.FlauntStrategy;
import mscw.common.api.CommonResult;
import mscw.common.api.ResultCode;
import mscw.common.domain.vo.UserVO;
import mscw.common.exception.ApiException;
import mscw.common.util.DictionaryOfUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

import static mcsw.offer.config.Constant.WATCH_COUNT_KEY;

/**
 * (Offer)表服务实现类
 *
 * @author Nan
 * @since 2022-12-29 14:33:58
 */
@Service
public class OfferService extends ServiceImpl<OfferDao, Offer> implements IService<Offer>, Common<Offer> {
    public MixBrowserVo convertToMixBrowserVo(Offer offer) {
        return MixBrowserVo.builder()
                .id(offer.getId())
                .createTime(DateTime.of(offer.getCreateTime()).toDateStr())
                .type(FlauntStrategy.Category.OFFER.getTag())
                .title(offer.getCompany())
                .subtitle(offer.getPositionName())
                .rightTitle(offer.getSalary())
                .remarks(offer.getRemarks().substring(0, 10))
                .userId(offer.getUserId())
                .build();
    }
}

