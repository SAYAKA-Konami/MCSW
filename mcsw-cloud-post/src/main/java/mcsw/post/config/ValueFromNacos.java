package mcsw.post.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

@ConfigurationProperties(prefix = "mcsw.post.properties")
@Configuration
@Getter
@Setter
@RefreshScope
public class ValueFromNacos {
    private Integer coreActive;

    private Integer maxActive;

    private Integer keepAliveSeconds;
    /**
     *  用于指定redis-cell中的漏斗参数
     */
    private Integer number;
    private Integer time;
    private Integer capacity;
}
