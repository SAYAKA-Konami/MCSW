package mcsw.account;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;

@SpringBootApplication
@EnableDiscoveryClient
@Slf4j
public class AccountApplication {
    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(AccountApplication.class, args);
        System.out.println("--------------------------------Swagger----------------------------------------------");
        Environment environment = context.getBean(Environment.class);
        String port = environment.getProperty("server.port");
        String path = environment.getProperty("server.servlet.context-path");
        InetAddress localHost = null;
        try {
            localHost = Inet4Address.getLocalHost();
        } catch ( UnknownHostException e) {
            log.error(e.getMessage(),e);
        }
        String ip = localHost.getHostAddress();  // 返回格式为：xxx.xxx.xxx

        System.out.println("Swagger：http://"+ ip + ":" + port + path +"/swagger-ui/");
    }
}
