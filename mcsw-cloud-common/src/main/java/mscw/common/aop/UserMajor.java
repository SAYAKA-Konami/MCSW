package mscw.common.aop;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @apiNote 该注解作用在第一个参数为HTTP请求头的方法上，会将用户的专业添加到请求头中。
 * @author wu nan
 * @since  2022/12/29
 **/
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface UserMajor {
}
