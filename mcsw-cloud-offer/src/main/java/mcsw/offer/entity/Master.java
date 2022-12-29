package mcsw.offer.entity;


import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * (Master)表实体类
 *
 * @author Nan
 * @since 2022-12-29 14:21:19
 */
@Data
@Accessors(chain = true)
public class Master implements GetId {

    @TableId(type = IdType.AUTO)
    private Integer id;

    private Integer college;
    //目标学校
    private String university;
    //目标专业
    private String masterMajor;
    private String bachelorMajor;
    // 考研专业课代码
    private Integer code;
    //0-考研 1-保研
    private Integer type;
    //考研总分
    private Integer score;
    //备注
    private String remarks;
    //发布人的Id
    private Integer userId;

    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @TableField(fill = FieldFill.UPDATE)
    private Date updateTime;

}

