package mcsw.account.dto;

import io.swagger.annotations.ApiModel;
import lombok.Data;

@ApiModel("用户注册信息")
@Data
public class UserDto {
    /**
     * 用户昵称
     */
    private String name;

    /**
     *  学号
     */
    private String id;

    /**
     *  登录学校信息门户的密码
     */
    private String passwd;
}
