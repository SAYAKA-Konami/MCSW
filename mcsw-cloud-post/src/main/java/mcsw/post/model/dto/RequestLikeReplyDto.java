package mcsw.post.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@ApiModel("点赞评论信息")
public class RequestLikeReplyDto {
    @NotNull
    @Schema(description = "评论id", required = true)
    private Integer replyId;
}
