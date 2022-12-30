package mcsw.offer.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import mcsw.offer.dao.CivilServantDao;
import mcsw.offer.entity.CivilServant;
import mcsw.offer.model.vo.MixBrowserVo;
import mcsw.offer.service.extend.startegy.flaunt.FlauntStrategy;
import org.springframework.stereotype.Service;

/**
 * (CivilServant)表服务实现类
 *
 * @author Nan
 * @since 2022-12-29 14:35:24
 */
@Service
public class CivilServantService extends ServiceImpl<CivilServantDao, CivilServant> implements IService<CivilServant>, Common<CivilServant> {

    @Override
    public MixBrowserVo convertToMixBrowserVo(CivilServant civilServant) {
        return MixBrowserVo.builder()
                .id(civilServant.getId())
                .title(civilServant.getPositionName())
                .rightTitle(civilServant.getSalary())
                .subtitle(civilServant.getCity())
                .type(FlauntStrategy.Category.CIVIL_SERVANT.getTag())
                .remarks(civilServant.getRemarks().substring(0, 10))
                .build();
    }

    @Override
    public int getCategory() {
        return FlauntStrategy.Category.CIVIL_SERVANT.getTag();
    }
}

