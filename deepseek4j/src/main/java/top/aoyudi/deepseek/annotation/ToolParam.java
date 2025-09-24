package top.aoyudi.deepseek.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 工具参数注解
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface ToolParam {
    /**
     * 参数名称
     * @return 参数名称
     */
    String name();

    /**
     * 参数类型
     * （string/number/boolean/object）
     * @return （string/number/boolean/object尽量别用/array）
     */
    String type();

    /**
     * 参数描述 大模式能清晰看懂
     * @return 参数描述
     */
    String description();

    /**
     * 是否必填
     * @return boolean
     */
    boolean required() default false;
}