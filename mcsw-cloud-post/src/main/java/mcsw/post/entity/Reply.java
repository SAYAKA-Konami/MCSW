package mcsw.post.entity;


import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * (Reply)表实体类
 *
 * @author Nan
 * @since 2022-11-30 11:06:35
 */
@Data
@Accessors(chain = true)
public class Reply{

    @TableId(type = IdType.AUTO)
    private Integer id;
    /**
     * 回复内容
     */
    private String replyContent;

    private Integer userId;

    private Integer postId;

    /**
     * 点赞数，默认为0
     */
    private Integer like;
    /**
     * 嵌套回复时标记父回复帖子的ID
     */
    private Integer parentId;

    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @TableField(fill = FieldFill.UPDATE)
    private Date updateTime;


}

