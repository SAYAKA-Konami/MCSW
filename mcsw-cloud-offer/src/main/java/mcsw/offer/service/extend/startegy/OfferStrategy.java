package mcsw.offer.service.extend.startegy;

import mcsw.offer.entity.Offer;
import mcsw.offer.model.dto.RequestOfferDto;
import mcsw.offer.model.dto.RequestShowOffDto;
import mcsw.offer.service.OfferService;
import mscw.common.api.CommonResult;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Map;

import static mcsw.offer.config.Constant.POST_FAIL;
import static mcsw.offer.config.Constant.POST_SUCCESS;

@Component
public class OfferStrategy implements FlauntStrategy {

    @Resource
    private OfferService offerService;

    @Override
    @Transactional
    public CommonResult<String> flaunt(Map<String, String> header, RequestShowOffDto requestShowOffDto) {
        RequestOfferDto offerDto = requestShowOffDto.getOfferDto();
        Offer offer = new Offer();
        int userId = Integer.parseInt(header.get("id"));
        String major = header.get("major");
        int college = Integer.parseInt(header.get("college"));
        offer.setUserId(userId).setCollege(college).setMajor(major)
                .setRemarks(requestShowOffDto.getRemarks())
                .setCity(offer.getCity())
                .setCompany(offerDto.getCompany())
                .setEducation(offerDto.getEducation())
                .setSalary(offerDto.getSalary())
                .setType(offerDto.getType())
                .setIndustry(offerDto.getIndustry())
                .setPositionName(offerDto.getPositionName());
        boolean save = offerService.save(offer);
        if (save) {
            return CommonResult.success(POST_SUCCESS);
        } else {
            return CommonResult.failed(POST_FAIL);
        }
    }

    @Override
    public int getCategory() {
        return Category.OFFER.getTag();
    }
}
