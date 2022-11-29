package mcsw.gateway.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureException;
import mscw.common.api.CommonResult;
import mscw.common.api.ResultCode;
import mscw.common.domain.dto.UserDto;

import static mscw.common.config.Constants.SECRET_KEY;


/**
 * @apiNote JWT 验证工具类
 * @author wu nan
 * @since  2022/11/28
 **/
public class JwtUtil {

    /**
     * 身份认证
     * @param jwt 令牌
     * @return 成功状态返回200，其它均为失败
     */
    public static CommonResult<UserDto> validationToken(String jwt) {
        try {
            //解析JWT字符串中的数据，并进行最基础的验证
            Claims claims = Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(jwt)
                .getBody();
            UserDto user = UserDto.builder().id(claims.get("id").toString())
                    .name(claims.get("name").toString())
                    .gender(claims.get("gender").toString())
                    .degreeCz(claims.get("degree").toString())
                    .collegeCz(claims.get("college").toString())
                    .major(claims.get("major").toString())
                    .email(claims.get("email") == null ? "" : claims.get("email").toString())
                    .build();
            return CommonResult.success(user);
        } catch (ExpiredJwtException e) {
            // 已过期令牌
            return CommonResult.failed(ResultCode.UNAUTHORIZED);
        } catch (SignatureException e) {
            // 伪造令牌
            return CommonResult.failed(ResultCode.FAILED);
        } catch (Exception e) {
            // 系统错误
            return CommonResult.failed(ResultCode.FAILED);
        }
    }

//    public static void main(String[] args){
//        String jwt = "eyJhbGciOiJIUzI1NiJ9.eyJleHAiOjE1ODkxMTcyNzcsImxvZ2luTmFtZSI6IjAwIiwibmlja05hbWUiOiIwMCIsImhlYWRJbWdVcmwiOiJodHRwOi8vcTk0aXN3ejM3LmJrdC5jbG91ZGRuLmNvbS9kZWZhdWx0LmpwZyIsInV1aWQiOiIyMjk0YzBiNmM2Y2Y0MjI1YWRmNTRhNzE2YzNmNDNiZSJ9.diWIKZPpefc5nfGa7S2u2eYIWbF1TYO-MUTHqQCtCFs";
//        Result<User> jwtValid = validationToken(jwt);
//        if (jwtValid.getCode() == 200) {
//            System.out.println(jwtValid.getData().toString());
//        }
//    }
}
