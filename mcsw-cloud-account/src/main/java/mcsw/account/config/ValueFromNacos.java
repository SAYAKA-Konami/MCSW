package mcsw.account.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
@Setter
@RefreshScope
@ConfigurationProperties(prefix = "mcsw.account")
public class ValueFromNacos {

    private Integer crawlerCoreActive;

    private Integer crawlerMaxActive;

    private Integer keepAliveSeconds;

    private String userAgent;
}
