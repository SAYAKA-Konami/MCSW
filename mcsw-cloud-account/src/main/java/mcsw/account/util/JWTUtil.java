package mcsw.account.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import mcsw.account.model.vo.UserVO;

import java.util.Date;

import static mcsw.account.config.Constant.SECRET_KEY;

public class JWTUtil {
    public static String buildJwt(Date expire, UserVO user) {
        String jwt = Jwts.builder()
                // 使用HS256加密算法
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                // 过期时间
                .setExpiration(expire)
                // 密码不参与Token的生成
                .claim("uuid",user.getUuid())
                .claim("id",user.getId())
                .claim("major", user.getMajor())
                .claim("name", user.getName())
                .claim("college", user.getCollege())
                .compact();
        return jwt;
    }
}
