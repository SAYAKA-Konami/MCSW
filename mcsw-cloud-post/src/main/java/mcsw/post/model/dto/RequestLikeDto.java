package mcsw.post.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@ApiModel("点赞")
public class RequestLikeDto {
    @NotNull
    @Schema(description = "帖子id或评论ID", required = true)
    private Integer id;

    @NotNull
    @Schema(description = "0表示点赞帖子，1表示点赞评论", required = true)
    private Integer type;
}
