package mcsw.post.service;

import cn.hutool.http.HttpStatus;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import mcsw.post.client.UserClient;
import mcsw.post.dao.ReplyDao;
import mcsw.post.entity.Reply;
import mcsw.post.model.dto.ReplyNestedDto;
import mcsw.post.model.dto.ReplyPostDto;
import mcsw.post.model.dto.RequestReplyDto;
import mcsw.post.task.QueryMapOfUserNameTask;
import mscw.common.aop.EnableRequestHeader;
import mscw.common.api.CommonResult;
import mscw.common.domain.vo.ReplyVo;
import mscw.common.domain.vo.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

import static mcsw.post.config.Constants.*;

/**
 * (Reply)表服务实现类
 *
 * @author Nan
 * @since 2022-11-30 11:06:52
 */
@Service
@Slf4j
public class ReplyService extends ServiceImpl<ReplyDao, Reply> implements IService<Reply> {

    private ReplyDao replyDao;

    private UserClient userClient;

    @Resource
    private ThreadPoolTaskExecutor postTaskExecutor;

    private RedisTemplate<String, Object> redisTemplate;

    private final ReentrantLock lock = new ReentrantLock();

    public int countReplyNumOfPost(Integer postId){
        return replyDao.countReplyNumOfPost(postId);
    }

    /**
     *  点赞评论。
     * @apiNote 先Redis中的内容，然后定时任务更新数据库。
     */
    public CommonResult<String> likeReply(Map<String, String> header, Integer replyId){
        String key = REPLY_LIKE_KEY_PREFIX + replyId;
        if (Boolean.TRUE.equals(redisTemplate.hasKey(key))){
            redisTemplate.opsForValue().increment(key);
        }else{
            lock.lock();
            try {
                if (Boolean.TRUE.equals(redisTemplate.hasKey(key))) {
                    // 如果此时缓存已经存在。直接执行自增操作即可
                    redisTemplate.opsForValue().increment(key);
                    return CommonResult.success(LIKE_SUCCESS);
                }
                // 查不到到数据库中查
                Reply reply = replyDao.selectById(replyId);
                if (!Objects.isNull(reply)) {
                    redisTemplate.opsForValue().set(key, reply.getLike() + 1);
                }else{
                    log.error("点赞评论出错。数据库中不存在该评论！");
                    return CommonResult.failed(SERVER_ERROR);
                }
            }finally {
                lock.unlock();
            }
        }
        // 将其添加到对应的点赞列表中
        redisTemplate.opsForSet().add(REPLY_LIKE_KEY_PREFIX + replyId, header.get("id"));
        return CommonResult.success(LIKE_SUCCESS);
    }

    /**
     *  评论
     * @apiNote 该方法包含了评论的两种情形
     */
    public CommonResult<String> reply(RequestReplyDto requestReplyDto){
        ReplyPostDto replyPostDto = requestReplyDto.getReplyPostDto();
        Reply reply = new Reply();
        if (Objects.isNull(replyPostDto)) {
            // 回复评论
            ReplyNestedDto replyNestedDto = requestReplyDto.getReplyNestedDto();
            reply.setReplyContent(replyNestedDto.getReplyContent())
                    .setPostId(replyNestedDto.getPostId())
                    .setParentId(replyNestedDto.getParentId())
                    .setLike(0);
            if (queryUserIdAndFillEntity(replyNestedDto.getUserName(), reply)) {
                return CommonResult.failed(SERVER_ERROR);
            }
        }else{
            // 回复帖子
            reply.setReplyContent(replyPostDto.getReplyContent())
                    .setPostId(replyPostDto.getPostId())
                    .setLike(0);
            if (queryUserIdAndFillEntity(replyPostDto.getUserName(), reply)) {
                return CommonResult.failed(SERVER_ERROR);
            }
        }
        return replyDao.insert(reply) == 1 ? CommonResult.success(REPLY_SUCCESS) : CommonResult.failed(SERVER_ERROR);
    }

    private boolean queryUserIdAndFillEntity(String userName, Reply reply) {
        CommonResult<UserVO> userInfo = userClient.getUserInfo(userName);
        if (userInfo.getCode() == HttpStatus.HTTP_OK) {
            String id = userInfo.getData().getId();
            reply.setUserId(Integer.parseInt(id));
        } else {
            log.error("查询用户信息失败，其返回了空结构体");
            return true;
        }
        return false;
    }

    /**
     *  组装帖子下的回复
     * @param postId 帖子ID
     */
    public List<ReplyVo> assemblePostWithReply(Integer postId){
        QueryWrapper<Reply> queryWrapper = new QueryWrapper<Reply>()
                .select("id", "post_id", "user_id", "reply_content", "like", "create_time", "update_time")
                .eq("post_id", postId);
        List<Reply> replies = replyDao.selectList(queryWrapper);
        QueryMapOfUserNameTask queryMapOfUserNameTask = new QueryMapOfUserNameTask(userClient, replies);
        Future<Optional<Map<String, String>>> mapFuture = postTaskExecutor.submit(queryMapOfUserNameTask);
        // 找出所有帖子的直接回复
        List<ReplyVo> topReply = replies.stream().filter(reply -> reply.getParentId() == null).map(this::convertReplyToVo).collect(Collectors.toList());
        Map<Integer, List<ReplyVo>> parentId_subReplys = Maps.newHashMapWithExpectedSize(replies.size() - topReply.size());
        replies.stream()
                .filter(reply -> reply.getParentId() != null)
                .forEach(subReply -> {
                    ReplyVo replyVo = convertReplyToVo(subReply);
                    parentId_subReplys.computeIfAbsent(subReply.getParentId(), k -> new ArrayList<>());
                    parentId_subReplys.get(subReply.getParentId()).add(replyVo);
                });
        CountDownLatch countDownLatch = new CountDownLatch(topReply.size());
        for (ReplyVo top : topReply) {
            postTaskExecutor.execute(() -> {
               recursiveAssembleReply(top, parentId_subReplys);
               countDownLatch.countDown();
            });
        }
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            log.error("countdownLatch等待组装回复时出错...");
            return null;
        }
        try {
            Optional<Map<String, String>> userIdToUserName = mapFuture.get();
            if (userIdToUserName.isPresent()) {
                Map<String, String> map = userIdToUserName.get();
                for (ReplyVo top : topReply) {
                    // 默认值为用户的ID，这里修改默认值
                    top.setUserName(map.get(top.getUserName()));
                    for (ReplyVo sub : top.getSubReply()) {
                        sub.setUserName(map.get(sub.getUserName()));
                    }
                }
            }else{
                log.error("查询用户信息失败。返回的信息是空...");
            }
        } catch (InterruptedException | ExecutionException e) {
            log.error("查询用户信息出错...");
            return topReply;
        }
        // 更新帖子的点赞数
        synReplyLike(topReply);
        return topReply;
    }

    /**
     *  如果Redis中存在数据。那么点赞数为Redis中的数据，否则以数据库中的数据为准
     */
    private void synReplyLike(List<ReplyVo> topReply) {
        for (ReplyVo replyVo : topReply) {
            String key = REPLY_LIKE_KEY_PREFIX + replyVo.getId();
            if (Boolean.TRUE.equals(redisTemplate.hasKey(key))) {
                replyVo.setLike((Integer) redisTemplate.opsForValue().get(key));
            }
            synReplyLike(replyVo.getSubReply());
        }
    }

    /**
     *  递归组装回复
     * @param replyVo 回复主题
     * @param parentId_subReplys 子回复的合集，key为其对应的父回复的ID
     */
    private void recursiveAssembleReply(ReplyVo replyVo, Map<Integer, List<ReplyVo>> parentId_subReplys){
        if (!parentId_subReplys.containsKey(replyVo.getId()))
            return;
        List<ReplyVo> replyVos = parentId_subReplys.get(replyVo.getId());
        replyVo.setSubReply(replyVos);
        for (ReplyVo sub : replyVos) {
            recursiveAssembleReply(sub, parentId_subReplys);
        }
    }

    private ReplyVo convertReplyToVo(Reply reply){
        ReplyVo replyVo = new ReplyVo();
        replyVo.setId(reply.getId())
                .setReplyContent(reply.getReplyContent())
                .setCreateTime(reply.getCreateTime())
                .setUpdateTime(reply.getUpdateTime())
                .setLike(reply.getLike());
        // 这里做兜底处理，默认名称设定为账号ID（即学号）
        replyVo.setUserName(reply.getUserId().toString());
        return replyVo;
    }


    @Autowired
    public void setReplyDao(ReplyDao replyDao) {
        this.replyDao = replyDao;
    }

    @Autowired
    public void setUserClient(UserClient userClient) {
        this.userClient = userClient;
    }

    @Resource
    public void setRedisTemplate(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }
}

