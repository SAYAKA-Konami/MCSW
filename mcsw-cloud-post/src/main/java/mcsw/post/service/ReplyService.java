package mcsw.post.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import mcsw.post.dao.ReplyDao;
import mcsw.post.entity.Reply;
import org.springframework.stereotype.Service;

/**
 * (Reply)表服务实现类
 *
 * @author Nan
 * @since 2022-11-30 11:06:52
 */
@Service
public class ReplyService extends ServiceImpl<ReplyDao, Reply> implements IService<Reply> {

}

