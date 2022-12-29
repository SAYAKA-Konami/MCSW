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
 * (Offer)实体类
 *
 * @author Nan
 * @since 2022-12-29 14:19:25
 */
@Data
@Accessors(chain = true)
public class Offer implements Serializable, GetId {
    private static final long serialVersionUID = 325258889077296440L;

    @TableId(type = IdType.AUTO)
    private Integer id;
    /**
     * 公司
     */
    private String company;

    private String city;
    /**
     * e.g. 20*16
     */
    private String salary;
    /**
     * 0-校招，1-实习
     */
    private Integer type;
    /**
     * 学历
     */
    private String education;
    /**
     * 0-IT|互联网|通信 1-金融 2-采购|贸易|交通|物流 3-生产|制造
     */
    private Integer industry;
    /**
     * 学院.映射待定
     */
    private Integer college;
    /**
     * 备注
     */
    private String remarks;

    /**
     *  专业
     */
    private String major;

    /**
     * 发布人的ID
     */
    private Integer userId;
    /**
     * 岗位名称
     */
    private String positionName;

    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @TableField(fill = FieldFill.UPDATE)
    private Date updateTime;


}
