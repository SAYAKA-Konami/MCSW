package mscw.common.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import mscw.common.domain.dto.RequestPage;

import javax.validation.constraints.NotNull;

@Data
@ApiModel("查询帖子")
public class QueryPosts {
    @NotNull
    @Schema(description = "分页参数")
    private RequestPage requestPage;

    @NotNull
    @Schema(description = "帖子类型。0-work,1-cs,2master")
    private Integer category;
    @Schema(description = "用户ID")
    private Integer userId;

    @Schema(description = "帖子Id")
    private Integer postId;

}
