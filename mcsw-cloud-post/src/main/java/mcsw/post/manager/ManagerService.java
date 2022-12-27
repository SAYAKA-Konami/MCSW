package mcsw.post.manager;

import lombok.extern.slf4j.Slf4j;
import mcsw.post.service.PostService;
import mcsw.post.service.ReplyService;
import mscw.common.api.CommonResult;
import mscw.common.domain.vo.PostVo;
import mscw.common.domain.vo.PostWithReply;
import mscw.common.domain.vo.ReplyVo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class ManagerService {

    private final PostService postService;

    private final ReplyService replyService;

    public ManagerService(PostService postService, ReplyService replyService) {
        this.postService = postService;
        this.replyService = replyService;
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

