package mscw.common.aop;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @apiNote 标注方法需要进行流量控制
 * @author wu nan
 * @since  2023/1/7
 **/
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Limit {
    /**
     *  参数最多为4个。从左到右分别是： key、capacity、number、time
     */
    String[] value() default "";
}
