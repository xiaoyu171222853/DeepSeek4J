package top.aoyudi.deepseek.handler;

import org.springframework.stereotype.Component;
import top.aoyudi.deepseek.annotation.ToolFunction;
import top.aoyudi.deepseek.entity.request.tool.DSReqFunction;
import top.aoyudi.deepseek.entity.request.tool.DSReqToolParameters;

import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 工具注册器
 */
@Component
public class ToolRegister {

    /**
     * 工具执行器
     */
    protected static final Map<String, ToolExecutor> tools = new ConcurrentHashMap<>();

    /**
     * 工具
     */
    protected static final Map<String, DSReqFunction> dsReqFunctionMap = new ConcurrentHashMap<>();

    /**
     * 自动注册工具
     * @param bean bean
     * @param method 方法
     * @param dsReqFunction 方法
     */
    protected static void registerTool(Object bean, Method method, DSReqFunction dsReqFunction) {
        String name = dsReqFunction.getName();
        tools.put(name, new ToolExecutor(bean, method));
        dsReqFunctionMap.put(name,dsReqFunction);
    }

    public static DSReqFunction getDsReqFunction(String functionName) {
        return dsReqFunctionMap.get(functionName);
    }
}
