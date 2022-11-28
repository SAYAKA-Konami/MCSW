package mcsw.account.model.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserVO implements Serializable {

    private static final Long serialVersionUID = 1L;

    /**
     * 唯一id，这里直接利用学号做标识码。
     */
    private String id;

    /**
     * 登录名
     */
    private String name;

    private String genderCz;

    private String email;

    private String collegeCz;

    private String degreeCz;

    /**
     *  专业名称
     */
    private String major;
}

