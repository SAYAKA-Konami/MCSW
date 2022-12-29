package mcsw.offer.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import mcsw.offer.dao.CommentDao;
import mcsw.offer.entity.Comment;
import org.springframework.stereotype.Service;

/**
 * (Comment)表服务实现类
 *
 * @author Nan
 * @since 2022-12-29 14:35:56
 */
@Service
public class CommentService extends ServiceImpl<CommentDao, Comment> implements IService<Comment> {

}

