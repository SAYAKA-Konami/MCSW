package mscw.common.domain.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;
import java.util.List;

@Data
@Accessors(chain = true)
public class ReplyVo {

    private Integer id;
    //回复内容
    private String replyContent;

    private String userName;

    private Date createTime;

    private Date updateTime;
    //点赞数，默认为0
    private Integer like;
    /**
     *  嵌套回复
     */
    private List<ReplyVo> subReply;
}
