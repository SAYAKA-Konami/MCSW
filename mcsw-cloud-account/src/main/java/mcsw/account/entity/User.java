package mcsw.account.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

@Data
@Accessors(chain = true)
public class User {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String account;

    private String passwd;

    private String name;

    private Integer gender;

    private String email;

    /**
     * 0- 数学与信息学院、软件学院
     * 1- 经济管理学院
     * 2-电子信息学院
     * 其他待定
     */
    private Integer college;

    /**
     *  0- 本科
     *  1-研究生
     *  2-博士生
     *  3-教职工
     */
    private Integer degree;

    /**
     *  专业名称
     */
    private String major;

    /**
     *  个人简介。长度不超过30
     */
    private String introduction;

    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @TableField(fill = FieldFill.UPDATE)
    private Date updateTime;

}
