package mcsw.post.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@ApiModel("回复评论")
@Builder
public class ReplyNestedDto {
    @Schema(name = "userName", description = "账号")
    @NotNull
    private String userName;

    @NotNull
    @Schema(name = "content", description = "内容")
    private String replyContent;

    @NotNull
    @Schema(name = "parentId", description = "被评论的ID")
    private Integer parentId;

    @NotNull
    @Schema(name = "postId", description = "被评论的帖子的ID")
    private Integer postId;
}
