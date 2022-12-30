package mcsw.offer.service.extend.startegy.getInfo;

import mcsw.offer.model.vo.MixFlauntVo;
import mcsw.offer.service.extend.startegy.CategoryOfStrategy;

import java.util.Optional;

public interface GetInfoStrategy extends CategoryOfStrategy {

    Optional<MixFlauntVo> getInfo(int id);

}
