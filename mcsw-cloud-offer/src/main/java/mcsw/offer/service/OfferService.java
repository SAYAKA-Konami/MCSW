package mcsw.offer.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import mcsw.offer.dao.OfferDao;
import mcsw.offer.entity.Offer;
import org.springframework.stereotype.Service;

/**
 * (Offer)表服务实现类
 *
 * @author Nan
 * @since 2022-12-29 14:33:58
 */
@Service
public class OfferService extends ServiceImpl<OfferDao, Offer> implements IService<Offer> {

}

