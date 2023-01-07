package mcsw.post.config;

import lombok.Data;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import javax.annotation.Resource;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicInteger;

@Data
@Configuration
public class ThreadPoolAutoConfiguration {

    @Resource
    private ValueFromNacos valueFromNacos;

    @Bean(name = "postTaskExecutor")
    @Lazy
    public ThreadPoolTaskExecutor getPostTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(valueFromNacos.getCoreActive());
        executor.setMaxPoolSize(valueFromNacos.getMaxActive());
        executor.setKeepAliveSeconds(valueFromNacos.getKeepAliveSeconds());
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.setThreadFactory(new ThreadFactory() {
            private AtomicInteger nextThreadId = new AtomicInteger();

            @Override
            public Thread newThread(Runnable runnable) {
                return new Thread(runnable, "post-worker-" + nextThreadId.incrementAndGet());
            }
        });
        return executor;
    }
}
