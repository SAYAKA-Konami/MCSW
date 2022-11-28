package mcsw.gateway.filter;

import lombok.extern.slf4j.Slf4j;
import mcsw.gateway.config.SystemPropertiesConfig;
import mcsw.gateway.utils.JwtUtil;
import mscw.common.api.CommonResult;
import mscw.common.api.ResultCode;
import mscw.common.domain.DictionaryOfCollegeAndDegree;
import mscw.common.domain.UserDto;
import mscw.common.service.RedisService;
import mscw.common.util.JsonUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;



/**
 * @apiNote Token验证
 * @author wu nan
 * @since  2022/11/28
 **/
@Order(0)
@Component
@Slf4j
public class AuthFilter implements GlobalFilter {

    @Autowired
    private RedisTemplate<Object, Object> redisTemplate;
    @Autowired
    private SystemPropertiesConfig systemPropertiesConfig;


    private static final String SUFFIX_MAJOR = "_major";

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // 白名单Path
        Set<String> whiteList = this.getWhiteList();
        String path = exchange.getRequest().getPath().toString();
        if (whiteList.contains(path)) {
            return chain.filter(exchange);
        }

        String[] segments = path.split("/");
        if (!whiteList.contains(segments[1])) {
            // 认证
            String token = exchange.getRequest().getHeaders().getFirst("token");
            CommonResult<UserDto> result = JwtUtil.validationToken(token);
            if (result.getCode() == ResultCode.SUCCESS.getCode()) {
                // 认证通过
                UserDto user = result.getData();
                Map<String, Integer> degreecz_code = DictionaryOfCollegeAndDegree.getDEGREECZ_CODE();
                Map<String, Integer> collegeName_code = DictionaryOfCollegeAndDegree.getCollegeName_code();
                // 放置到Redis中
                redisTemplate.opsForValue().set(user.getId() + SUFFIX_MAJOR, user.getMajor());
                // 追加请求头用户信息
                Consumer<HttpHeaders> httpHeaders = httpHeader -> {
                    httpHeader.set("id", user.getId());
                    httpHeader.set("name",user.getName());
                    httpHeader.set("gender", user.getGender());
                    // 以下传的都是字典
                    httpHeader.set("degree", degreecz_code.get(user.getDegreeCz()).toString());
                    httpHeader.set("college", collegeName_code.get(user.getCollegeCz()).toString());
                };
                ServerHttpRequest serverHttpRequest = exchange.getRequest()
                        .mutate()
                        .headers(httpHeaders)
                        .build();
                exchange.mutate().request(serverHttpRequest).build();
                return chain.filter(exchange);
            }

            // 认证过期、失败，均返回401
            ServerHttpResponse response = exchange.getResponse();
            byte[] bits = null;
            try {
                bits = JsonUtil.toJson(result).getBytes(StandardCharsets.UTF_8);
            } catch (Exception e) {
                log.error("序列化成字符串时出现问题...对象为:{}", result);
                bits = new byte[0];
            }
            DataBuffer buffer = response.bufferFactory().wrap(bits);
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            // 指定编码，否则在浏览器中会中文乱码
            response.getHeaders().add("Content-Type", "text/plain;charset=UTF-8");
            return response.writeWith(Mono.just(buffer));
        }
        return chain.filter(exchange);
    }

    /**
     * 请求白名单
     */
    private Set<String> getWhiteList(){
        String whitelists = this.systemPropertiesConfig.getWhitelist();
        if (StringUtils.isEmpty(whitelists)) {
            return new HashSet<>();
        }
        String[] whiteArray = whitelists.split(",");
        return new HashSet<>(Arrays.asList(whiteArray));
    }
}
