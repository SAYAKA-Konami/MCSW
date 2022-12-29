package mcsw.offer.service.extend.browser;

import cn.hutool.http.HttpStatus;
import lombok.extern.slf4j.Slf4j;
import mcsw.offer.client.UserClient;
import mcsw.offer.entity.GetId;
import mcsw.offer.model.vo.MixBrowserVo;
import mcsw.offer.service.Common;
import mscw.common.api.CommonResult;
import mscw.common.api.ResultCode;
import mscw.common.domain.vo.UserVO;
import mscw.common.exception.ApiException;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import static mcsw.offer.config.Constant.WATCH_COUNT_KEY;

@Slf4j
public class GetMixBrowser<T extends GetId, U extends Common<T>> {
    private final UserClient userClient;

    private final ThreadPoolTaskExecutor offerTaskExecutor;

    private final StringRedisTemplate redisTemplate;

    private final U service;

    public GetMixBrowser(UserClient userClient, ThreadPoolTaskExecutor offerTaskExecutor, StringRedisTemplate redisTemplate, U service) {
        this.userClient = userClient;
        this.offerTaskExecutor = offerTaskExecutor;
        this.redisTemplate = redisTemplate;
        this.service = service;
    }

    public Map<Integer, MixBrowserVo> get(List<T> entities, List<Integer> userIds){
        // key-userId value:userVo
        CompletableFuture<Map<Integer, UserVO>> getUserVos = CompletableFuture.supplyAsync(() -> {
            CommonResult<List<UserVO>> userByIds = userClient.getUserByIds(userIds);
            if (userByIds.getCode() == HttpStatus.HTTP_OK) {
                List<UserVO> data = userByIds.getData();
                Map<Integer, UserVO> userVOMap = data.stream().collect(Collectors.toMap(userVO -> Integer.parseInt(userVO.getId()), userVO -> userVO));
                return userVOMap;
            } else {
                log.error("获取用户信息失败");
                throw new ApiException(ResultCode.FAILED);
            }
        }, offerTaskExecutor);
        CompletableFuture<Map<Integer, MixBrowserVo>> getMixBrowserVoMap = CompletableFuture.supplyAsync(() -> {
            Map<Integer, MixBrowserVo> mixBrowserVoMap = entities.stream().collect(Collectors.toMap(GetId::getId, service::convertToMixBrowserVo));
            return mixBrowserVoMap;
        }, offerTaskExecutor);
        CompletableFuture<Map<Integer, MixBrowserVo>> result = getUserVos.thenCombine(getMixBrowserVoMap, (userMap, mixBrowserMap) -> {
            mixBrowserMap.forEach((k, v) -> {
                Integer userId = v.getUserId();
                UserVO userVO = userMap.get(userId);
                v.setCollege(userVO.getCollegeCz());
                v.setDegree(userVO.getDegreeCz());
                Double score = redisTemplate.opsForZSet().score(WATCH_COUNT_KEY, v.getId());
                v.setWatched(score.intValue());
            });
            return mixBrowserMap;
        });
        Map<Integer, MixBrowserVo> join = result.join();
        return join;
    }
}
