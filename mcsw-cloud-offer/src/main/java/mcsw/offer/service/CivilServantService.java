package mcsw.offer.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import mcsw.offer.dao.CivilServantDao;
import mcsw.offer.entity.CivilServant;
import org.springframework.stereotype.Service;

/**
 * (CivilServant)表服务实现类
 *
 * @author Nan
 * @since 2022-12-29 14:35:24
 */
@Service
public class CivilServantService extends ServiceImpl<CivilServantDao, CivilServant> implements IService<CivilServant> {

}

