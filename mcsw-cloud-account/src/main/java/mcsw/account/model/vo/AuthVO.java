package mcsw.account.model.vo;

import lombok.Data;

@Data
public class AuthVO {
    private String token;
    private UserVO user;
}
