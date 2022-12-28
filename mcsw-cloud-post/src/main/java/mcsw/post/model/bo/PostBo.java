package mcsw.post.model.bo;

import lombok.Data;

import java.util.Date;

/**
 * @apiNote 包含帖子的回复数和点赞数
 * @author wu nan
 * @since  2022/12/28
 **/
@Data
public class PostBo {
    private Integer id;

    private String title;

    private String content;
    /**
     * 发帖用户的ID
     */
    private Integer userId;
    /*
     * 点赞数。创建时为0
     */
    private Integer like;
    /**
     * 0-工作 1-公务员 2-考研 3- 保研
     */
    private Integer category;

    private Date createTime;

    private Date updateTime;
    /**
     *  回复数
     */
    private Integer replyNum;
}
