package mcsw.offer.service.extend.startegy;

import mcsw.offer.entity.CivilServant;
import mcsw.offer.model.dto.RequestCsDto;
import mcsw.offer.model.dto.RequestShowOffDto;
import mcsw.offer.service.CivilServantService;
import mscw.common.api.CommonResult;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;

import static mcsw.offer.config.Constant.POST_FAIL;
import static mcsw.offer.config.Constant.POST_SUCCESS;

@Component
public class CsStrategy implements FlauntStrategy {

    @Resource
    private CivilServantService civilServantService;
    @Override
    public CommonResult<String> flaunt(Map<String, String> header, RequestShowOffDto requestShowOffDto) {
        int userId = Integer.parseInt(header.get("id"));
        Integer college = Integer.parseInt(header.get("college"));
        String major = header.get("major");
        CivilServant civilServant = new CivilServant();
        RequestCsDto csDto = requestShowOffDto.getCsDto();
        civilServant.setUserId(userId).setCollege(college).setMajor(major)
                .setRemarks(requestShowOffDto.getRemarks())
                .setCity(csDto.getCity())
                .setSalary(csDto.getSalary())
                .setPositionName(csDto.getPositionName());
        boolean save = civilServantService.save(civilServant);
        if (save) {
            return CommonResult.success(POST_SUCCESS);
        } else {
            return CommonResult.failed(POST_FAIL);
        }
    }

    @Override
    public int getCategory() {
        return Category.CIVIL_SERVANT.getTag();
    }
}
