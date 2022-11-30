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
    @Schema(description = "用户ID", required = true)
    private Integer id;

    @NotNull
    @Schema(description = "帖子类型。0-工作，1-公务员，2-考研, 3-保研", required = true)
    private Integer category;

    @NotNull
    @Schema(description = "帖子标题", required = true)
    private String title;

    @NotNull
    @Schema(description = "帖子内容", required = true)
    private String content;
}
