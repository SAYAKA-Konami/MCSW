package mcsw.post.manager;

import lombok.extern.slf4j.Slf4j;
import mcsw.post.model.dto.RequestLikeDto;
import mcsw.post.service.PostService;
import mcsw.post.service.ReplyService;
import mscw.common.aop.EnableRequestHeader;
import mscw.common.api.CommonResult;
import mscw.common.domain.vo.PostVo;
import mscw.common.domain.vo.PostWithReply;
import mscw.common.domain.vo.ReplyVo;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static mcsw.post.config.Constants.*;

@Service
@Slf4j
public class ManagerService {

    @Resource
    private PostService postService;
    @Resource
    private ReplyService replyService;
    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    public CommonResult<String> like(Map<String, String> header, RequestLikeDto requestLikeDto){
        Optional<String> opKey = getKey(requestLikeDto);
        if (opKey.isPresent()) {
            String key = opKey.get();
            String userId = header.get("id");
            // 校验是否已经点赞过...
            if (Boolean.TRUE.equals(redisTemplate.opsForSet().isMember(key, userId))) {
                return CommonResult.failed(HAS_LIKED);
            }
            if (requestLikeDto.getType() == 0) {
                return postService.likePost(header, requestLikeDto.getId());
            }else{
                return replyService.likeReply(header, requestLikeDto.getId());
            }
        }else{
            return CommonResult.failed(WRONG_PARAM);
        }
    }

    private Optional<String> getKey(RequestLikeDto requestLikeDto){
        // 0表示点赞帖子
        if (requestLikeDto.getType() == 0) {
            return Optional.of(POST_LIKE_USER_LIST_KEY_PREFIX + requestLikeDto.getId());
        }else if (requestLikeDto.getType() == 1){
            return Optional.of(REPLY_LIKE_USER_LIST_KEY_PREFIX + requestLikeDto.getId());
        }else{
            return Optional.empty();
        }
    }

    /**
     *  发送在点击相应帖子后
     *  获取帖子的所有回复
     */
    public CommonResult<PostWithReply> getFullInfoOfPost(Integer postId){
        PostVo postVo = postService.getPostById(postId);
        List<ReplyVo> replyVos = replyService.assemblePostWithReply(postId);
        PostWithReply postWithReply = new PostWithReply(postVo, replyVos);
        return CommonResult.success(postWithReply);
    }


}

