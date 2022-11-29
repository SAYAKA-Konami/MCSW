package mcsw.gateway.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "system.properties")
@RefreshScope
public class SystemPropertiesConfig {
    /** 请求白名单 */
    private String whitelist;
}
