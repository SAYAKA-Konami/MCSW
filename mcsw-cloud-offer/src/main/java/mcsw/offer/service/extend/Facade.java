package mcsw.offer.service.extend;

import lombok.Getter;
import mcsw.offer.service.extend.startegy.FlauntStrategy;
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

    @Bean("flauntStrategyMap")
    public Map<Integer, FlauntStrategy> buildStrategyFacade(){
        Map<Integer, FlauntStrategy> map = new HashMap<>();
        for (FlauntStrategy flauntStrategy : strategyList) {
            map.put(flauntStrategy.getCategory(), flauntStrategy);
        }
        return map;
    }


    @Autowired
    public void setStrategyList(List<FlauntStrategy> strategyList) {
        this.strategyList = strategyList;
    }
}
