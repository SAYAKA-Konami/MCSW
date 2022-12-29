package mcsw.offer.manage;

import mcsw.offer.service.CivilServantService;
import mcsw.offer.service.MasterService;
import mcsw.offer.service.OfferService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class ManageService {
    @Resource
    private MasterService masterService;

    @Resource
    private CivilServantService civilServantService;

    @Resource
    private OfferService offerService;
}
