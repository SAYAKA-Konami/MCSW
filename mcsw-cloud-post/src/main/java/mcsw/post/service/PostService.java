package mcsw.post.service;

import cn.hutool.core.collection.ListUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import mcsw.post.client.UserClient;
import mcsw.post.dao.PostDao;
import mcsw.post.entity.Post;
import mcsw.post.model.bo.PostBo;
import mcsw.post.model.dto.PostDto;
import mscw.common.aop.EnableRequestHeader;
import mscw.common.aop.Limit;
import mscw.common.api.CommonResult;
import mscw.common.api.ResultCode;
import mscw.common.domain.dto.QueryPosts;
import mscw.common.domain.dto.RequestPage;
import mscw.common.domain.vo.PostVo;
import mscw.common.domain.vo.UserVO;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

import static mcsw.post.config.Constants.*;

/**
 * (Post)表服务实现类
 *  Redis 记录对应帖子的点赞用户 key:postId - value set of user id.
 *  考虑将帖子的点赞数、评论数都放置到Redis中。特别是点赞，频繁的读写会对数据库造成一定上的压力...
 * @author Nan
 * @since 2022-11-30 10:58:06
 */
@Service
public class PostService extends ServiceImpl<PostDao, Post> implements IService<Post> {
    private PostDao postDao;
    private UserClient userClient;
    private RedisTemplate<Object, Object> redisTemplate;
    private ReplyService replyService;
    private ThreadPoolTaskExecutor postTaskExecutor;

    private final ReentrantLock lock = new ReentrantLock();


    /**
     * 用户发帖
     */
    public CommonResult<String> insertNewPost(PostDto postDto){
        Post post = new Post();
        // 默认点赞数为0
        post.setLike(0).setContent(postDto.getContent()).setTitle(postDto.getTitle())
                .setUserId(postDto.getId()).setCategory(postDto.getCategory());
        return this.save(post) ? CommonResult.success(POST_SUCCESS) : CommonResult.failed(POST_FAILED);
    }

    /**
     *  获取当前用户的所有帖子
     */
    @EnableRequestHeader
    public CommonResult<IPage<PostVo>> getUserPosts(Map<String, String> header, RequestPage requestPage){
        int id = Integer.parseInt(header.get("id"));
        List<PostBo> postBos = postDao.queryFullInfoOfPost(id);
        List<PostVo> postVos = convertPostBoToVo(postBos);
        IPage<PostVo> page = Page.of(requestPage.getCurrent(), requestPage.getSize());
        page.setRecords(postVos);
        return CommonResult.success(page);
    }



    /**
     *  获取指定用户的所有帖子
     */
    public CommonResult<IPage<PostVo>> getSpecifyUserPosts(String name, RequestPage requestPage){
        CommonResult<UserVO> userInfo = userClient.getUserInfo(name);
        if (userInfo.getCode() != ResultCode.SUCCESS.getCode()) {
            return CommonResult.failed(USER_NOT_FOUND);
        }
        int userId = Integer.parseInt(userInfo.getData().getId());
        List<PostBo> postBos = postDao.queryFullInfoOfPost(userId);
        List<PostVo> postVos = convertPostBoToVo(postBos);
        IPage<PostVo> page = Page.of(requestPage.getCurrent(), requestPage.getSize());
        page.setRecords(postVos);
        return CommonResult.success(page);
    }

    /**
     * 点赞帖子
     */
    public CommonResult<String> likePost(Map<String, String> header, Integer postId){
        String key = POST_LIKE_KEY_PREFIX + postId;
        if (Boolean.TRUE.equals(redisTemplate.hasKey(key))){
            redisTemplate.opsForValue().increment(key);
        }else{
            lock.lock();
            try {
                /*
                    这里类似单例模式中的Double check。多个用户线程可能都进入了try语句之后阻塞。
                    第一个进入临界区的线程成功地将数据写回缓存之后，其他访问线程无需再访问数据库资源。直接在Redis中操作数据即可。
                 */
                if (Boolean.TRUE.equals(redisTemplate.hasKey(key))) {
                    // 如果此时缓存已经存在。直接执行自增操作即可
                    redisTemplate.opsForValue().increment(key);
                    return CommonResult.success(LIKE_SUCCESS);
                }
                // 查不到到数据库中查
                Post post = postDao.selectById(postId);
                if (!Objects.isNull(post)) {
                    redisTemplate.opsForValue().set(key, post.getLike() + 1);
                }else{
                    log.error("点赞评论出错。数据库中不存在该评论！");
                    return CommonResult.failed(SERVER_ERROR);
                }
            }finally {
                lock.unlock();
            }
        }
        // 将其添加到对应的点赞列表中
        redisTemplate.opsForSet().add(POST_LIKE_USER_LIST_KEY_PREFIX + postId, header.get("id"));
        return CommonResult.success(LIKE_SUCCESS);
    }

    /**
     *  根据帖子id获取帖子信息并转化成PostVO
     */
    public PostVo getPostById(Integer postId){
        Post post = postDao.selectById(postId);
        List<Post> of = ListUtil.of(post);
        return statisticsReplyNumAndConvertIntoPostVo(of).get(0);
    }

    /**
     *  查询主页帖子
     * @apiNote 在ES引入前，暂且使用mysql来代替该功能
     */
    @Limit(value = {"home", "1000", "1000", "60"})
    public CommonResult<List<PostVo>> getHomePage(QueryPosts queryPosts){
        List<PostBo> postBos = postDao.queryHomepage(queryPosts);
        List<PostVo> result = convertPostBoToVo(postBos);
        return CommonResult.success(result);
    }

    @NotNull
    private static List<PostVo> convertPostBoToVo(List<PostBo> postBos) {
        List<PostVo> result = new ArrayList<>(postBos.size());
        for (PostBo postBo : postBos) {
            PostVo postVo = new PostVo();
            BeanUtils.copyProperties(postBo, postVo);
            result.add(postVo);
        }
        return result;
    }


    /**
     *  统计帖子的评论数并转换为PostVo
     *
     * @param posts 帖子
     */
    @NotNull
    @Deprecated
    private List<PostVo> statisticsReplyNumAndConvertIntoPostVo(List<Post> posts) {
        List<Integer> postIds = posts.stream().map(Post::getId).collect(Collectors.toList());
        // 为啥这里需要并发执行？因为每个帖子的回复都是各自独立的。所以需要分别统计。感觉这里设计得似乎有些不合理...
        CompletableFuture<PostVo>[] fillReplyNumFutureList = new CompletableFuture[postIds.size()];
        for (int i = 0; i < postIds.size(); i++) {
            Post post = posts.get(i);
            Integer postId = post.getId();
            fillReplyNumFutureList[i] = CompletableFuture.supplyAsync(() -> {
                PostVo postVo = new PostVo();
                // 回复表的帖子Id（post_id)应该也需要有索引...
                postVo.setReplyNum(replyService.countReplyNumOfPost(postId));
                BeanUtils.copyProperties(post, postVo);
                return postVo;
            }, postTaskExecutor);
        }
        CompletableFuture.allOf(fillReplyNumFutureList).join();
        return Arrays.stream(fillReplyNumFutureList).map(CompletableFuture::join).collect(Collectors.toList());
    }

    @Autowired
    public void setPostDao(PostDao postDao) {
        this.postDao = postDao;
    }
    @Autowired
    public void setUserClient(UserClient userClient) {
        this.userClient = userClient;
    }
    @Autowired
    public void setRedisTemplate(RedisTemplate<Object, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }
    @Resource
    public void setReplyService(ReplyService replyService) {
        this.replyService = replyService;
    }

    @Resource
    public void setPostTaskExecutor(ThreadPoolTaskExecutor postTaskExecutor) {
        this.postTaskExecutor = postTaskExecutor;
    }
}

