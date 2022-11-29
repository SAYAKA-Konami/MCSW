package mscw.common.domain;

import lombok.*;

import java.util.List;


@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {
    /**
     * 用户昵称
     */

    private String name;

    private String gender;

    /**
     *  学号
     */
    private String id;

    /**
     *  登录学校信息门户的密码
     */
    private String passwd;


    private String email;

    /**
     *  转换由后端处理
     */
    private Integer college;

    private String collegeCz;

    private String major;

    private String degreeCz;

    private Integer degree;
    private Integer status;
    private String clientId;
    private List<String> roles;

}
