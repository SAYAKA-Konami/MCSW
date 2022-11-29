package mscw.common.domain.vo;

import lombok.Data;

@Data
public class AuthVO {
    private String token;
    private UserVO user;
}
