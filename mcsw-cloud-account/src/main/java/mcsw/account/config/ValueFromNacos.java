package mcsw.account.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

@Configuration("ncaos")
@Getter
@Setter
@RefreshScope
public class ValueFromNacos {

    @Value("${mcsw.account.crawlerCoreActive}")
    private Integer crawlerCoreActive;

    @Value("${mcsw.account.crawlerMaxActive}")
    private Integer crawlerMaxActive;

    @Value("${mcsw.account.keepAliveSeconds}")
    private Integer keepAliveSeconds;

    @Value("${mcsw.account.userAgent}")
    private String userAgent;
}
