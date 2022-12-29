package mcsw.offer.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import mcsw.offer.dao.MasterDao;
import mcsw.offer.entity.Master;
import org.springframework.stereotype.Service;

/**
 * (Master)表服务实现类
 *
 * @author Nan
 * @since 2022-12-29 14:35:13
 */
@Service
public class MasterService extends ServiceImpl<MasterDao, Master> implements IService<Master> {

}

