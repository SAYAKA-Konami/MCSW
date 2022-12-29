package mcsw.offer.service.extend.startegy.getInfo;

import mcsw.offer.model.vo.MixFlauntVo;
import mcsw.offer.service.extend.startegy.CategoryOfStrategy;
import mscw.common.api.CommonResult;

public interface GetInfoStrategy extends CategoryOfStrategy {

    CommonResult<MixFlauntVo> getInfo(int id);

}
