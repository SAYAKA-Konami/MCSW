package mcsw.post.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@ApiModel("点赞")
public class RequestLikePostDto {
    @NotNull
    @Schema(description = "帖子id", required = true)
    private Integer postId;
}
