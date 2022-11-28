package mscw.common.aop;

import mscw.common.domain.DictionaryOfCollegeAndDegree;
import mscw.common.service.RedisService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;
import java.util.Optional;

/**
 * @apiNote 转换请求头中的学院和专业和学位。
 * @author wu nan
 * @since  2022/11/28
 **/
@Aspect
@Component
public class GlobalRequestHeaderHandler {

    private static final String SUFFIX_MAJOR = "_major";
    @Resource
    private RedisService redisServiceImpl;

    /**
     *  第一个参数为哈希表的方法
     */
    @Pointcut("args(java.util.Map,..)")
    public void collegeAndDegreeAndMajor() {}
    @Pointcut("@target(org.springframework.web.bind.annotation.RestController)")
    public void controller(){}

    @Before("collegeAndDegreeAndMajor() && controller()")
    public void convert(JoinPoint joinPoint){
        Map<String, String> header = (Map<String, String>)joinPoint.getArgs()[0];
        String college = DictionaryOfCollegeAndDegree.getCode2collegeName().get(Integer.parseInt(header.get("college")));
        header.put("college", college);
        String degree = DictionaryOfCollegeAndDegree.getCODE_DEGREECZ().get(Integer.parseInt(header.get("degree")));
        header.put("degree", degree);
        Optional.ofNullable(redisServiceImpl.get(header.get("id") + SUFFIX_MAJOR))
                .ifPresent(major -> header.put("major", major.toString()));
    }
}
