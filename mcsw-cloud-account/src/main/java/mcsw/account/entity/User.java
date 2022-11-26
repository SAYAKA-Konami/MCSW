package mcsw.account.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.experimental.Accessors;

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

}
