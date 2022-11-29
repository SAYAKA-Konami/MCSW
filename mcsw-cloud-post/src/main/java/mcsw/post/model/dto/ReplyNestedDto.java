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
    @Schema(name = "account", description = "账号")
    @NotNull
    private String account;

    @NotNull
    @Schema(name = "content", description = "内容")
    private String content;

    @NotNull
    @Schema(name = "parentId", description = "评论的ID")
    private Integer parentId;
}
