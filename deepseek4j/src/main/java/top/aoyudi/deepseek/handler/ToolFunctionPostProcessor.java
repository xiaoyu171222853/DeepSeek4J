package top.aoyudi.deepseek.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;
import top.aoyudi.deepseek.annotation.ToolFunction;
import top.aoyudi.deepseek.annotation.ToolParam;
import top.aoyudi.deepseek.entity.request.tool.DSReqFunction;
import top.aoyudi.deepseek.entity.request.tool.DSReqToolParameters;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;

/**
 * 工具后处理器
 * 注册工具
 */
@Component
public class ToolFunctionPostProcessor implements BeanPostProcessor {
    /**
     * 扫描带有 @ToolFunction的方法进行工具方法 注册
     * @param bean bean
     * @param beanName beanName
     * @return Object
     * @throws BeansException BeansException
     */
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        // 扫描 Bean 中所有带有 @ToolFunction 注解的方法
        for (Method method : bean.getClass().getDeclaredMethods()) {
            ToolFunction annotation = method.getAnnotation(ToolFunction.class);
            if (annotation != null) {
                // 构造参数说明
                // 1. 构建参数Schema
                DSReqToolParameters parameters = buildParameters(method);

                // 2. 构造工具函数定义
                DSReqFunction function = new DSReqFunction();
                function.setName(method.getName());
                function.setDescription(annotation.description());
                function.setParameters(parameters);

                // 注册工具
                ToolRegister.registerTool(bean, method, function);
            }
        }
        return bean;
    }

    /**
     * 根据方法参数构建DeepSeek参数Schema
     */
    private DSReqToolParameters buildParameters(Method method) {
        DSReqToolParameters parameters = new DSReqToolParameters();
        List<String> requiredParams = new ArrayList<>();

        // 遍历方法参数
        for (Parameter param : method.getParameters()) {
            ToolParam paramAnn = param.getAnnotation(ToolParam.class);
            if (paramAnn == null) continue;

            // 构建参数属性
            DSReqToolParameters.Property prop = new DSReqToolParameters.Property(
                    paramAnn.type(),
                    paramAnn.description()
            );

            parameters.getProperties().put(paramAnn.name(), prop);

            // 标记必填参数
            if (paramAnn.required()) {
                requiredParams.add(paramAnn.name());
            }
        }

        parameters.setRequired(requiredParams);
        return parameters;
    }
}
