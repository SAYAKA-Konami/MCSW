package mcsw.post.entity;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * (Post)表实体类
 *
 * @author Nan
 * @since 2022-11-30 11:33:04
 */
@Data
@Accessors(chain = true)
public class Post {

    @TableId(type = IdType.AUTO)
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

    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @TableField(fill = FieldFill.UPDATE)
    private Date updateTime;

}

