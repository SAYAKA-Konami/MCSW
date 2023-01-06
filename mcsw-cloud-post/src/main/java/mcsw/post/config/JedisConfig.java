package mcsw.post.config;

import mscw.common.util.JedisUtil;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisPool;

@ConfigurationProperties(prefix = "mcsw.redis.cell")
@Configuration
@RefreshScope
public class JedisConfig {
    /**
     * 以下两个变量的存在是因为redis-cell所在的redis可能是独立部署。
     */
    private String url;

    private Integer port;

    @Bean("jedisUtil")
    public JedisUtil generateJedisUtil(){
        JedisPool jedisPool = new JedisPool(url, port);
        return new JedisUtil(jedisPool);
    }

}
