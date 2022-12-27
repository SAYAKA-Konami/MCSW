package mcsw.post.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import mcsw.post.client.UserClient;
import mcsw.post.config.ThreadPoolAutoConfiguration;
import mcsw.post.dao.ReplyDao;
import mcsw.post.entity.Reply;
import mcsw.post.task.QueryMapOfUserName;
import mscw.common.domain.vo.ReplyVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

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

    public int countReplyNumOfPost(Integer postId){
        return replyDao.countReplyNumOfPost(postId);
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
        QueryMapOfUserName queryMapOfUserName = new QueryMapOfUserName(userClient, replies);
        Future<Map<String, String>> mapFuture = postTaskExecutor.submit(queryMapOfUserName);
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
            Map<String, String> userIdToUserName = mapFuture.get();
            for (ReplyVo top : topReply) {
                // 默认值为用户的ID，这里修改默认值
                top.setUserName(userIdToUserName.get(top.getUserName()));
                for (ReplyVo sub : top.getSubReply()) {
                    sub.setUserName(userIdToUserName.get(sub.getUserName()));
                }
            }
        } catch (InterruptedException | ExecutionException e) {
            log.error("查询用户信息出错...");
            return topReply;
        }
        return topReply;
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
}

