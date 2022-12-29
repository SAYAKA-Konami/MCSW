package mcsw.offer.service.extend;

import lombok.Getter;
import mcsw.offer.service.extend.startegy.flaunt.FlauntStrategy;
import mcsw.offer.service.extend.startegy.getInfo.GetInfoStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
@Getter
public class Facade{

    private List<FlauntStrategy> strategyList = new ArrayList<>();

    private List<GetInfoStrategy> getInfoStrategyList = new ArrayList<>();

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

    @Autowired
    public void setGetInfoStrategyList(List<GetInfoStrategy> getInfoStrategyList) {
        this.getInfoStrategyList = getInfoStrategyList;
    }

    @Autowired
    public void setStrategyList(List<FlauntStrategy> strategyList) {
        this.strategyList = strategyList;
    }
}
