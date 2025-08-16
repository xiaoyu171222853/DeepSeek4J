package top.aoyudi.deepseek.handler;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Getter;
import top.aoyudi.deepseek.annotation.ToolParam;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

import static top.aoyudi.deepseek.handler.ToolRegister.tools;

/**
 * 工具执行器——调用executeTool手动执行
 */
@Getter
@AllArgsConstructor
public class ToolExecutor {
    /**
     * bean对象
     */
    private final Object bean;
    /**
     * 方法
     */
    private final Method method;

    /**
     * 序列化对象
     */
    private static final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 执行工具（请手动执行）
     * @param name 方法名
     * @param argumentsJson 参数JSON
     * @return 执行结果
     * @throws Exception exception
     */
    public static Object executeTool(String name, String argumentsJson) throws Exception {
        ToolExecutor executor = tools.get(name);
        if (executor == null) {
            throw new IllegalStateException("工具不存在，请确保调用工具名与@ToolFunction的name保持一致");
        }
        Method method = executor.getMethod();
        method.setAccessible(true);
        // 无参数方法
        if (method.getParameterCount() == 0) {
            // 执行方法
            return method.invoke(executor.getBean());
        }
        // 解析参数
        Object[] args = parseArguments(method, argumentsJson);
        // 执行方法
        return method.invoke(executor.getBean(), args);
    }

    /**
     * 解析参数
     * @param method 方法
     * @param json 参数JSON_String
     * @return 参数 Object... args
     * @throws Exception
     */
    private static Object[] parseArguments(Method method, String json) throws Exception {
        JsonNode rootNode = objectMapper.readTree(json);
        Parameter[] parameters = method.getParameters();
        Object[] args = new Object[parameters.length];

        for (int i = 0; i < parameters.length; i++) {
            Parameter param = parameters[i];
            ToolParam annotation = param.getAnnotation(ToolParam.class);

            // 获取JSON字段名（优先用注解的name，其次用参数名）
            String fieldName = (annotation != null && !annotation.name().isEmpty())
                    ? annotation.name()
                    : param.getName();

            // 参数提取和校验
            if (!rootNode.has(fieldName)) {
                if (annotation != null && annotation.required()) {
                    throw new IllegalArgumentException("缺少必要参数: " + fieldName);
                }
                args[i] = null; // 非必填参数设为null
                continue;
            }

            // 类型转换
            args[i] = objectMapper.convertValue(
                    rootNode.get(fieldName),
                    param.getType()
            );
        }
        return args;
    }
}