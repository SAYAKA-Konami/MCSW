package mcsw.post.job;

import cn.hutool.core.collection.CollectionUtil;
import lombok.extern.slf4j.Slf4j;
import mcsw.post.dao.PostDao;
import mcsw.post.dao.ReplyDao;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.*;

import static mcsw.post.config.Constants.POST_LIKE_KEY_PREFIX;
import static mcsw.post.config.Constants.REPLY_LIKE_KEY_PREFIX;

@Component
@Slf4j
public class SynReplyLikeNumJob {

    private  RedisTemplate<String, Integer> redisTemplate;

    private  ReplyDao replyDao;

    private  PostDao postDao;

    /**
     *  每隔一小时同步一次
     */
    @Scheduled(cron = "0 0 0/1 * * ?")
    public void synReplyLikeNum() {
        log.info("同步评论点赞数开始");
        Set<String> keys = redisTemplate.keys(REPLY_LIKE_KEY_PREFIX.concat("*"));
        if (CollectionUtil.isEmpty(keys)) {
            log.info("没有需要同步的评论点赞数");
            return;
        }
        List<Map<String, Object>> listForUpdating = mapForUpdate(keys);
        replyDao.updateLikeNumBatch(listForUpdating);
        log.info("同步评论点赞结束");
    }


    @Scheduled(cron = "0 0 0/1 * * ?")
    public void synPostLikeNum(){
        log.info("同步帖子点赞数开始");
        Set<String> keys = redisTemplate.keys(POST_LIKE_KEY_PREFIX.concat("*"));
        if (CollectionUtil.isEmpty(keys)) {
            log.info("没有需要同步的帖子点赞数");
            return;
        }
        List<Map<String, Object>> listForUpdating = mapForUpdate(keys);
        postDao.updateLikeNumBatch(listForUpdating);
        log.info("同步帖子点赞数完成");
    }

    /**
     * 组装mybatis批量更新的数据结构
     * @param keys redis中的key的前缀
     */
    @NotNull
    private List<Map<String, Object>> mapForUpdate(Set<String> keys) {
        List<Map<String, Object>> listForUpdating = new ArrayList<>(keys.size());
        for (String key : keys) {
            String replyId = key.substring(REPLY_LIKE_KEY_PREFIX.length());
            Integer likeNum = redisTemplate.opsForValue().get(key);
            Map<String, Object> map = new HashMap<>();
            map.put("num", likeNum);
            map.put("id", replyId);
            listForUpdating.add(map);
        }
        return listForUpdating;
    }

    @Resource
    public void setRedisTemplate(RedisTemplate<String, Integer> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Autowired
    public void setReplyDao(ReplyDao replyDao) {
        this.replyDao = replyDao;
    }

    @Autowired
    public void setPostDao(PostDao postDao) {
        this.postDao = postDao;
    }
}
