package mcsw.post.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicInteger;

@Data
@Configuration
@ConfigurationProperties(prefix = "mcsw.post.properties")
@RefreshScope
public class ThreadPoolAutoConfiguration {

    private Integer coreActive;

    private Integer maxActive;

    private Integer keepAliveSeconds;

    @Bean(name = "postTaskExecutor")
    @Lazy
    public ThreadPoolTaskExecutor getPostTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(coreActive);
        executor.setMaxPoolSize(maxActive);
        executor.setKeepAliveSeconds(keepAliveSeconds);
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.setThreadFactory(new ThreadFactory() {
            private AtomicInteger nextThreadId = new AtomicInteger();

            @Override
            public Thread newThread(Runnable runnable) {
                return new Thread(runnable, "account-worker-" + nextThreadId.incrementAndGet());
            }
        });
        return executor;
    }
}
