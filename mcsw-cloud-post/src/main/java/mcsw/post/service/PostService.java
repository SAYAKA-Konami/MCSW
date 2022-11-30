package mcsw.post.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import mcsw.post.client.UserClient;
import mcsw.post.dao.PostDao;
import mcsw.post.entity.Post;
import mcsw.post.model.dto.PostDto;
import mscw.common.aop.EnableRequestHeader;
import mscw.common.api.CommonResult;
import mscw.common.api.ResultCode;
import mscw.common.domain.vo.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

import static mcsw.post.config.Constants.USER_NOT_FOUND;

/**
 * (Post)表服务实现类
 *
 * @author Nan
 * @since 2022-11-30 10:58:06
 */
@Service
public class PostService extends ServiceImpl<PostDao, Post> implements IService<Post> {


    private PostDao postDao;

    private UserClient userClient;

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

    /**
     *  获取当前用户的所有帖子
     */
    @EnableRequestHeader
    public CommonResult<IPage<Post>> getUserPosts(Map<String, String> header, IPage<Post> page){
        int id = Integer.parseInt(header.get("id"));
        IPage<Post> postIPage = postDao.selectAllByUserId(page, id);
        return CommonResult.success(postIPage);
    }

    /**
     *  获取指定用户的所有帖子
     */
    public CommonResult<IPage<Post>> getSpecifyUserPosts(String name, IPage<Post> page){
        CommonResult<UserVO> userInfo = userClient.getUserInfo(name);
        if (userInfo.getCode() != ResultCode.SUCCESS.getCode()) {
            return CommonResult.failed(USER_NOT_FOUND);
        }
        IPage<Post> postIPage = postDao.selectAllByUserId(page, Integer.parseInt(userInfo.getData().getId()));
        return CommonResult.success(postIPage);
    }


    @Autowired
    public void setPostDao(PostDao postDao) {
        this.postDao = postDao;
    }

    @Autowired
    public void setUserClient(UserClient userClient) {
        this.userClient = userClient;
    }
}

