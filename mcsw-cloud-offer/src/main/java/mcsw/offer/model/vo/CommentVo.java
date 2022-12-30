package mcsw.offer.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("拼装的评论")
public class CommentVo {
    @Schema(description = "评论ID")
    private Integer id;

    @Schema(description = "评论。限制60字数")
    private String content;

    @Schema(description = "发表评论的用户昵称")
    private String userName;

    private String createTime;

    @Schema(description = "嵌套评论")
    private List<CommentVo> subComment;
}
