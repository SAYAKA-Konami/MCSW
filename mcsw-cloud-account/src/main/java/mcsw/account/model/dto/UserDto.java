package mcsw.account.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("用户注册信息")
@Data
public class UserDto {
    /**
     * 用户昵称
     */
    @ApiModelProperty("昵称")
    private String name;

    @ApiModelProperty("性别。0-女/ 1-男")
    private Integer gender;

    /**
     *  学号
     */
    @ApiModelProperty("学号")
    private String id;

    /**
     *  登录学校信息门户的密码
     */
    @ApiModelProperty("学校信息门户的登录密码")
    private String passwd;

    @ApiModelProperty("学院")
    private Integer college;

    @ApiModelProperty("邮箱")
    private String email;




}