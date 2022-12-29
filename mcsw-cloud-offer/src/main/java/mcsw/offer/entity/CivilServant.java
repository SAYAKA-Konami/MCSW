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
 * (CivilServant)实体类
 *
 * @author Nan
 * @since 2022-12-29 14:26:21
 */
@Data
@Accessors(chain = true)
public class CivilServant implements Serializable, GetId {
    private static final long serialVersionUID = 257575043981420589L;

    @TableId(type = IdType.AUTO)
    private Integer id;

    private Integer college;

    private String city;

    private Integer userId;
    /**
     * 专业
     */
    private String major;
    /**
     * 岗位名称。可以不填。
     */
    private String positionName;
    /**
     * 薪酬，选填
     */
    private String salary;

    /**
     * 备注
     */
    private String remarks;


    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @TableField(fill = FieldFill.UPDATE)
    private Date updateTime;


}
