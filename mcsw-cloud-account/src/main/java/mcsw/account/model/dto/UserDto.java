package mcsw.account.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@ApiModel("用户注册信息")
@Data
public class UserDto {
    /**
     * 用户昵称
     */
    @ApiModelProperty("昵称")
    private String name;

    @ApiModelProperty("性别。0-女/ 1-男/-1-未定义")
    private Integer gender;

    /**
     *  学号
     */
    @ApiModelProperty("学号")
    @NotNull
    private String id;

    /**
     *  登录学校信息门户的密码
     */
    @ApiModelProperty("学校信息门户的登录密码")
    @NotNull
    private String passwd;

    @ApiModelProperty("邮箱")
    @Email(message = "邮箱格式错误")
    private String email;

    /**
     *  转换由后端处理
     */
    @ApiModelProperty(value = "学院", hidden = true)
    private Integer college;

    @ApiModelProperty(value = "学院中文")
    private String collegeCz;


    @ApiModelProperty("专业")
    private String major;

    @ApiModelProperty("本科/研究生/博士/教职工")
    private String degreeCz;


    @ApiModelProperty(hidden = true)
    private Integer degree;

}
