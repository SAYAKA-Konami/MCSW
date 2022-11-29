package mscw.common.aop;

import java.lang.annotation.*;
/**
 * @apiNote 自定义注解，用于标记需要转换学院和专业和学位的方法
 * @author wu nan
 * @since  2022/11/29
 **/
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface EnableRequestHeader {
}
