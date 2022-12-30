package mcsw.offer.service.extend;

import lombok.Getter;
import mcsw.offer.client.UserClient;
import mcsw.offer.entity.GetId;
import mcsw.offer.service.Common;
import mcsw.offer.service.extend.browser.GetMixBrowser;
import mcsw.offer.service.extend.startegy.flaunt.FlauntStrategy;
import mcsw.offer.service.extend.startegy.getInfo.GetInfoStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
@Getter
public class Facade<T extends GetId>{
    private List<FlauntStrategy> strategyList = new ArrayList<>();
    private List<GetInfoStrategy> getInfoStrategyList = new ArrayList<>();
    private List<Common<T>> getMixBrowserList = new ArrayList<>();

    private UserClient userClient;
    private ThreadPoolTaskExecutor offerTaskExecutor;
    private StringRedisTemplate redisTemplate;

    @Bean("flauntStrategyMap")
    public Map<Integer, FlauntStrategy> buildStrategyFacade(){
        Map<Integer, FlauntStrategy> map = new HashMap<>();
        for (FlauntStrategy flauntStrategy : strategyList) {
            map.put(flauntStrategy.getCategory(), flauntStrategy);
        }
        return map;
    }

    @Bean("getInfoStrategyMap")
    public Map<Integer, GetInfoStrategy> buildGetInfoFacade(){
        Map<Integer, GetInfoStrategy> map = new HashMap<>();
        for (GetInfoStrategy getInfoStrategy : getInfoStrategyList) {
            map.put(getInfoStrategy.getCategory(), getInfoStrategy);
        }
        return map;
    }

    @Bean("getMixBrowserMap")
    public Map<Integer, GetMixBrowser<T>> getMixBrowserMap(){
        Map<Integer, GetMixBrowser<T>> map = new HashMap<>();
        for (Common<T> uCommon : getMixBrowserList) {
            map.put(uCommon.getCategory(), new GetMixBrowser<>(userClient, offerTaskExecutor, redisTemplate, uCommon));
        }
        return map;
    }

    @Autowired
    public void setGetInfoStrategyList(List<GetInfoStrategy> getInfoStrategyList) {
        this.getInfoStrategyList = getInfoStrategyList;
    }
    @Autowired
    public void setStrategyList(List<FlauntStrategy> strategyList) {
        this.strategyList = strategyList;
    }
    @Autowired
    public void setGetMixBrowserList(List<Common<T>> getMixBrowserList) {
        this.getMixBrowserList = getMixBrowserList;
    }
    @Autowired
    public void setUserClient(UserClient userClient) {
        this.userClient = userClient;
    }
    @Autowired
    public void setOfferTaskExecutor(ThreadPoolTaskExecutor offerTaskExecutor) {
        this.offerTaskExecutor = offerTaskExecutor;
    }
    @Autowired
    public void setRedisTemplate(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }
}
