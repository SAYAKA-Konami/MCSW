package mcsw.offer.entity;


import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * (Comment)表实体类
 *
 * @author Nan
 * @since 2022-12-29 14:24:13
 */
@Data
@Accessors(chain = true)
public class Comment{

    @TableId(type = IdType.AUTO)
    private Integer id;

    private Integer offerId;

    private Integer masterId;

    private Integer csId;

    private Integer parentId;

    //评论。限制60字数
    private String content;

    private Integer userId;
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @TableField(fill = FieldFill.UPDATE)
    private Date updateTime;
}

