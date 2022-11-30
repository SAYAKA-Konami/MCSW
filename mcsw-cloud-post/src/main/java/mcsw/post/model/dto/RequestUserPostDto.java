package mcsw.post.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@ApiModel("请求用户的帖子信息")
public class RequestUserPostDto {

    @NotNull
    @Schema(description = "用户ID", required = true)
    private Integer id;
}
