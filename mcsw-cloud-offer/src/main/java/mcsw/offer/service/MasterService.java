package mcsw.offer.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import mcsw.offer.dao.MasterDao;
import mcsw.offer.entity.Master;
import mcsw.offer.model.vo.MixBrowserVo;
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

    // TODO
    @Override
    public MixBrowserVo convertToMixBrowserVo(Master master) {
        return null;
    }
}

