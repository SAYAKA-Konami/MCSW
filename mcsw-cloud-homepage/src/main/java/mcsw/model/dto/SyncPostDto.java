package mcsw.model.dto;

import lombok.Data;

import java.util.Date;

@Data
public class SyncPostDto {
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
     *  回复数
     */
    private Integer replyNum;
    /**
     * 0-工作 1-公务员 2-考研 3- 保研
     */
    private Integer category;

    private Date createTime;

    private Date updateTime;

}
