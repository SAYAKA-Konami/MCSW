package mcsw.offer.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import mcsw.offer.dao.MasterDao;
import mcsw.offer.entity.Master;
import mcsw.offer.model.vo.MixBrowserVo;
import mcsw.offer.service.extend.startegy.flaunt.FlauntStrategy;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * (Master)表服务实现类
 *
 * @author Nan
 * @since 2022-12-29 14:35:13
 */
@Service
public class MasterService extends ServiceImpl<MasterDao, Master> implements IService<Master>, Common<Master> {

    private final static String RECOMMEND = "保研";

    @Override
    public MixBrowserVo convertToMixBrowserVo(Master master) {
        return MixBrowserVo.builder()
                .id(master.getId())
                .type(FlauntStrategy.Category.MASTER.getTag())
                .title(master.getUniversity())
                .subtitle(master.getMasterMajor())
                .rightTitle(master.getType() == 0 ? master.getScore().toString() : RECOMMEND)
                .remarks(master.getRemarks().substring(0, 10)).build();
    }

    @Override
    public int getCategory() {
        return FlauntStrategy.Category.MASTER.getTag();
    }
}

