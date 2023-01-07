package mcsw.post.config;

import mscw.common.aop.Limit;
import mscw.common.api.ResultCode;
import mscw.common.exception.ApiException;
import mscw.common.util.JedisUtil;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Aspect
@Component
public class LimitConfig {

    @Resource
    private JedisUtil jedisUtil;
    @Resource
    private ValueFromNacos valueFromNacos;

    @Pointcut("@annotation(mscw.common.aop.Limit)")
    public void execute(){}


    @Before(value = "execute()")
    public void gate(JoinPoint joinPoint){
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Limit annotation = signature.getMethod().getAnnotation(Limit.class);
        String[] value = annotation.value();
        JedisUtil.CellAnswer cell;
        if (value != null && value.length == 4){
             cell = jedisUtil.cell(value);
        }else{
            // 如果没有指定参数则采用默认策略
            String key = signature.getName();
            String capacity = valueFromNacos.getCapacity().toString();
            String number = valueFromNacos.getNumber().toString();
            String time = valueFromNacos.getTime().toString();
            cell = jedisUtil.cell(key, capacity, number, time);
        }
        // 1表示超过限制...可能是Rust中奇怪的设定？亦或者是redis-cell的作者奇怪的设定
        if (cell.getResult() == 1) {
            // 间接中断方法执行
            throw new ApiException(ResultCode.LIMIT);
        }
    }

}
