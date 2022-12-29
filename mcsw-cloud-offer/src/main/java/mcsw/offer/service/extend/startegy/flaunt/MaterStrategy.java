package mcsw.offer.service.extend.startegy.flaunt;

import mcsw.offer.entity.Master;
import mcsw.offer.model.dto.RequestMasterDto;
import mcsw.offer.model.dto.RequestShowOffDto;
import mcsw.offer.service.MasterService;
import mscw.common.api.CommonResult;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;

import static mcsw.offer.config.Constant.POST_FAIL;
import static mcsw.offer.config.Constant.POST_SUCCESS;

@Component
public class MaterStrategy implements FlauntStrategy {

    @Resource
    private MasterService masterService;

    @Override
    public CommonResult<String> flaunt(Map<String, String> header, RequestShowOffDto requestShowOffDto) {
        int userId = Integer.parseInt(header.get("id"));
        Integer college = Integer.parseInt(header.get("college"));
        String major = header.get("major");
        RequestMasterDto masterDto = requestShowOffDto.getMasterDto();
        Master master = new Master();
        master.setUserId(userId).setCollege(college).setBachelorMajor(major)
                .setRemarks(requestShowOffDto.getRemarks())
                .setScore(masterDto.getScore())
                .setUniversity(masterDto.getUniversity())
                .setMasterMajor(masterDto.getMasterMajor())
                .setType(masterDto.getType());
        boolean save = masterService.save(master);
        if (save) {
            return CommonResult.success(POST_SUCCESS);
        } else {
            return CommonResult.failed(POST_FAIL);
        }
    }

    @Override
    public int getCategory() {
        return Category.MASTER.getTag();
    }
}
