package mscw.common.domain.dto;

import lombok.*;

import java.util.List;


@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {

    /**
     * 数据库ID
     */
    private String id;

    /**
     * 用户昵称
     */
    private String name;

    private String gender;

    /**
     *  学号
     */
    private String account;

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
    /**
     *  0-代表当前对象内无内容。1-代表对象内有内容。
     */
    private Integer status;
    private String clientId;
    private List<String> roles;

}
