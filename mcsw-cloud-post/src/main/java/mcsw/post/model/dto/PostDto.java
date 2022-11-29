package mcsw.post.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@ApiModel("帖子信息")
@Builder
public class PostDto {

    @NotNull
    @Schema(name = "account", description = "账号/学号")
    private String account;

    @NotNull
    @Schema(name = "title", description = "帖子标题")
    private String title;

    @NotNull
    @Schema(name = "content", description = "帖子内容")
    private String content;
}
