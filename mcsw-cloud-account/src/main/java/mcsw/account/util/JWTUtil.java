package mcsw.account.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import mscw.common.domain.vo.UserVO;
import java.util.Date;
import static mscw.common.config.Constants.SECRET_KEY;

public class JWTUtil {
    public static String buildJwt(Date expire, UserVO user) {
        String jwt = Jwts.builder()
                // 使用HS256加密算法
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                // 过期时间
                .setExpiration(expire)
                // 密码不参与Token的生成
                .claim("id",user.getId())
                .claim("account", user.getAccount())
                .claim("name", user.getName())
                .claim("gender", user.getGenderCz())
                .claim("degree", user.getDegreeCz())
                .claim("college", user.getCollegeCz())
                .claim("major", user.getMajor())
                .claim("email", user.getEmail())
                .compact();
        return jwt;
    }
}
