package mcsw.post.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@ApiModel("帖子评论信息")
@Builder
public class ReplyPostDto {

    @NotNull
    @Schema(name = "account", description = "账号/学号")
    private String account;

    @Schema(name = "content", description = "评论内容")
    @NotNull
    private String content;

    @NotNull
    @Schema(name = "postId", description = "被评论的帖子的ID")
    private Integer postId;
}
