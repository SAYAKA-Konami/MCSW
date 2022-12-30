package mcsw.offer.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@ApiModel("发表评论")
public class RequestCommentDto {
    @Schema(description = "帖子ID")
    @NotNull
    private Integer offerId;

    @Schema(description = "帖子类型。 0-offer, 1-master, 2-cs")
    @NotNull
    @Range(min = 0, max = 2, message = "参数错误")
    private Integer type;

    @Schema(description = "父评论的ID")
    @NotNull
    private Integer parentId;

    @Schema(description = "评论内容。长度不得超过60")
    @NotNull
    @Size(min = 1, max = 60, message = "长度不得超过60")
    private String content;
}
