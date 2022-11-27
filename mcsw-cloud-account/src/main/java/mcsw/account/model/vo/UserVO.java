package mcsw.account.model.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserVO implements Serializable {

    private static final Long serialVersionUID = 1L;

    private Integer id;

    /**
     * 唯一id，这里直接利用学号做标识码。
     */
    private String uuid;

    /**
     * 登录名
     */
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

