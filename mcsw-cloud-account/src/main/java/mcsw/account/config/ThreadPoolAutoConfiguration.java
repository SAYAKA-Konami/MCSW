package mcsw.account.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import javax.annotation.Resource;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @apiNote 线程池
 * @author wu nan
 * @since  2022/11/26
 **/
@Configuration
@RefreshScope
public class ThreadPoolAutoConfiguration {

    @Resource
    ValueFromNacos value;

    @Bean(name = "accountTaskExecutor")
    @Lazy
    public ThreadPoolTaskExecutor getAccountTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(value.getCrawlerCoreActive());
        executor.setMaxPoolSize(value.getCrawlerMaxActive());
        executor.setKeepAliveSeconds(value.getKeepAliveSeconds());
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
