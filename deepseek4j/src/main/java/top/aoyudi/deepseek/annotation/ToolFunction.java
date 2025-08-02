package top.aoyudi.deepseek.annotation;

import java.lang.annotation.*;

/**
 * 工具方法注解
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ToolFunction {
    /**
     * 功能描述
     * @return 功能描述
     */
    String description();
}
