package mscw.common.aop;

import com.google.common.base.Stopwatch;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Aspect
@Component
@Slf4j
public class ScheduledAop {
    /**
     * 大坑——AOP横切Controller层会导致Tomcat无法启动
     */

    @Pointcut("@annotation(org.springframework.scheduling.annotation.Scheduled)")
    public void annotation() {
    }

    @Around("annotation()")
    public Object statisticAndLog(ProceedingJoinPoint joinPoint) {
        String name = joinPoint.getSignature().getName();
        log.info("{}开始执行", name);
        Stopwatch stopwatch = Stopwatch.createStarted();
        Object proceed = null;
        stopwatch.start();
        try {
            proceed = joinPoint.proceed();
        } catch (Throwable e) {
            log.error("aop统计代码耗时出错...");
        }
        stopwatch.stop();
        log.info("{}方法执行的耗时为：{} ms", name, stopwatch.elapsed(TimeUnit.MICROSECONDS));
        return proceed;
    }

}
