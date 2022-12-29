package mcsw.offer.manage;

import mcsw.offer.model.dto.RequestShowOffDto;
import mcsw.offer.service.CivilServantService;
import mcsw.offer.service.MasterService;
import mcsw.offer.service.OfferService;
import mcsw.offer.service.extend.Facade;
import mcsw.offer.service.extend.startegy.FlauntStrategy;
import mscw.common.aop.EnableRequestHeader;
import mscw.common.aop.UserMajor;
import mscw.common.api.CommonResult;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

@Service
public class ManageService {
    @Resource
    private MasterService masterService;

    @Resource
    private CivilServantService civilServantService;

    @Resource
    private OfferService offerService;

    @Resource
    private Map<Integer, FlauntStrategy> flauntStrategyMap;

    /**
     *  晒出offer或上岸记录
     * @param header 包含发布者的基础信息
     */
    @UserMajor
    public CommonResult<String> flaunt(Map<String, String> header, RequestShowOffDto requestShowOffDto){
        FlauntStrategy flauntStrategy = flauntStrategyMap.get(requestShowOffDto.getCategory());
        return flauntStrategy.flaunt(header, requestShowOffDto);
    }

}
