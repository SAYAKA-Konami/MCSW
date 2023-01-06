package mcsw.post.config;

import mscw.common.util.JedisUtil;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Aspect
@Component
public class LimitConfig {

    @Resource
    private JedisUtil jedisUtil;

    // TODO: 制定限流策略。


}
