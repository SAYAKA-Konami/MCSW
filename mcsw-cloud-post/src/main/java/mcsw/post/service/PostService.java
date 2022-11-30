package mcsw.post.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import mcsw.post.dao.PostDao;
import mcsw.post.entity.Post;
import mcsw.post.model.dto.PostDto;
import mscw.common.aop.EnableRequestHeader;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * (Post)表服务实现类
 *
 * @author Nan
 * @since 2022-11-30 10:58:06
 */
@Service
public class PostService extends ServiceImpl<PostDao, Post> implements IService<Post> {

    /**
     * 用户发帖
     */
    public boolean insertNewPost(PostDto postDto){
        Post post = new Post();
        // 默认点赞数为0
        post.setLike(0).setContent(postDto.getContent()).setTitle(postDto.getTitle())
                .setUserId(postDto.getId()).setCategory(postDto.getCategory());
        return this.save(post);
    }

}

