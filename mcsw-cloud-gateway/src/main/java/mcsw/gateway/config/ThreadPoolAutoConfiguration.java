package mcsw.gateway.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicInteger;

@Configuration
@RefreshScope
public class ThreadPoolAutoConfiguration {

    @Value("${mcsw.gateway.gatewayCoreActive:5}")
    private Integer gatewayCoreActive;

    @Value("${mcsw.gateway.gatewayMaxActive:10}")
    private Integer gatewayMaxActive;

    @Value("${mcsw.gateway.keepAliveSeconds:60}")
    private Integer keepAliveSeconds;

    @Bean(name = "gatewayTaskExecutor")
    @Lazy
    public ThreadPoolTaskExecutor getGatewayTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(gatewayCoreActive);
        executor.setMaxPoolSize(gatewayMaxActive);
        executor.setKeepAliveSeconds(keepAliveSeconds);
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.setThreadFactory(new ThreadFactory() {
            private AtomicInteger nextThreadId = new AtomicInteger();

            @Override
            public Thread newThread(Runnable runnable) {
                return new Thread(runnable, "gateway-worker-" + nextThreadId.incrementAndGet());
            }
        });
        return executor;
    }
}
