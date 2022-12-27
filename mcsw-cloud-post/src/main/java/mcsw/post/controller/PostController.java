package mcsw.post.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import mcsw.post.manager.ManagerService;
import mcsw.post.model.dto.*;
import mcsw.post.service.PostService;
import mcsw.post.service.ReplyService;
import mscw.common.api.CommonResult;
import mscw.common.domain.dto.RequestPage;
import mscw.common.domain.vo.PostVo;
import mscw.common.domain.vo.PostWithReply;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/post")
@Slf4j
public class PostController {

    private final ManagerService managerService;

    private final PostService postService;

    private final ReplyService replyService;

    public PostController(ManagerService managerService, PostService postService, ReplyService replyService) {
        this.managerService = managerService;
        this.postService = postService;
        this.replyService = replyService;
    }

    @PostMapping("/fullInfo")
    @ApiOperation(value = "获取帖子内容")
    public CommonResult<PostWithReply> getFullInfoOfPost(@RequestBody RequestPostInfoDto requestPostInfoDto){
        return managerService.getFullInfoOfPost(requestPostInfoDto.getPostId());
    }

    @PostMapping("/post")
    @ApiOperation("发帖")
    public CommonResult insertNewPost(@RequestBody PostDto postDto){
        return CommonResult.success(postService.insertNewPost(postDto));
    }

    @PostMapping("/getUserPosts")
    @ApiOperation("获取登录用户的帖子")
    public CommonResult<IPage<PostVo>> getUserPosts(@RequestHeader Map<String, String> header, @RequestBody RequestPage requestPage){
        return postService.getUserPosts(header, requestPage);
    }

    @PostMapping("/getPostsByUserName")
    @ApiOperation("获取指定用户的帖子")
    public CommonResult<IPage<PostVo>> getSpecifyUserPosts(@RequestBody RequestPostByUserNameDto requestPostByUserNameDto){
        return postService.getSpecifyUserPosts(requestPostByUserNameDto.getName(), requestPostByUserNameDto.getRequestPage());
    }

    @PostMapping("/likePost")
    @ApiOperation("点赞")
    public CommonResult<String> likePost(@RequestHeader Map<String, String> header, @RequestBody RequestLikeDto requestLikeDto){
        return managerService.like(header, requestLikeDto);
    }

    @PostMapping("/reply")
    @ApiOperation("评论")
    public CommonResult<String> reply(@RequestBody RequestReplyDto requestReplyDto){
        return replyService.reply(requestReplyDto);
    }

    @PostMapping("/likeReply")
    @ApiOperation("评论点赞")
    public CommonResult<String> likeReply(@RequestHeader Map<String, String> header, @RequestBody RequestLikeDto requestLikeDto){
        return managerService.like(header, requestLikeDto);
    }
}
