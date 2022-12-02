package mcsw.post.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import mscw.common.domain.vo.UserVO;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("帖子信息")
public class PostVo {

    @Schema(name = "postId", description = "帖子Id")
    private Integer postId;

    @Schema(name = "title", description = "帖子标题")
    private String title;

    @Schema(name = "content", description = "帖子内容")
    private String content;

    @JsonFormat(pattern="yyyy-MM-dd" ,timezone="GMT+8")
    @Schema(name = "createTime", description = "发帖时间")
    private Date createTime;

    @Schema(name = "userVo", description = "发帖用户信息")
    private UserVO userVO;

    @Schema(description = "点赞数")
    private Integer like;

    @Schema(description = "回复数")
    private Integer replyNum;
    @Schema(description = "帖子类别。0-work,1-cs,2master")
    private Integer category;

}
