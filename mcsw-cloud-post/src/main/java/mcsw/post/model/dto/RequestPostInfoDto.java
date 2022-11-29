package mcsw.post.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@ApiModel("请求具体帖子的信息")
public class RequestPostInfoDto {
    @Schema(name = "postId", description = "帖子Id")
    @NotNull
    private Integer postId;
}
