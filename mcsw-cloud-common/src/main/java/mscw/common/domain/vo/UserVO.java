package mscw.common.domain.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserVO implements Serializable {

    private static final Long serialVersionUID = 1L;

    /**
     * 唯一id
     */
    private String id;

    /**
     *  账号。这里是学号
     */
    private String account;

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

