package top.xiaoyu.deepseekdemo.service;

/**
 * 工具调用服务示例
 */
public interface IUtilService {
    /**
     * 测试工具调用
     * @param content 对话内容
     * @return 返回值
     * @throws Exception 异常
     */
    String testTool(String content) throws Exception;
}
