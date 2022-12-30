package mcsw.offer.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * (Comment)实体类
 *
 * @author Nan
 * @since 2022-12-30 13:52:10
 */
@Data
@Accessors(chain = true)
public class Comment implements Serializable {
    private static final long serialVersionUID = -71734684846465791L;

    @TableId(type = IdType.AUTO)
    private Integer id;
    /**
     * 帖子ID + 类别 e.g. 1o/1m/1c
     */
    private String mcswId;

    private Integer parentId;

    /**
     * 评论。限制60字数
     */
    private String content;

    private Integer userId;
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @TableField(fill = FieldFill.UPDATE)
    private Date updateTime;

}
