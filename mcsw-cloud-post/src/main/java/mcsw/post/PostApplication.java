package mcsw.post;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;

@SpringBootApplication
@EnableAspectJAutoProxy
@EnableDiscoveryClient
@EnableFeignClients
@EnableScheduling
@ComponentScan(basePackages = {"mcsw.post", "mscw.common"})
@Slf4j
public class PostApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(PostApplication.class, args);
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
