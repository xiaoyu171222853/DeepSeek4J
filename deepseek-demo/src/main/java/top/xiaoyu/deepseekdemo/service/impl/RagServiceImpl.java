package top.xiaoyu.deepseekdemo.service.impl;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.aoyudi.deepseek.entity.model.DeepSeekChatModel;
import top.aoyudi.deepseek.entity.request.DeepSeekRequest;
import top.aoyudi.deepseek.entity.request.message.DSUserReqMessage;
import top.aoyudi.deepseek.entity.response.DSResponse;
import top.aoyudi.deepseek.service.DeepSeekService;
import top.aoyudi.rag.util.RagUtils;

import java.util.Arrays;

@Log4j2
@Service
public class RagServiceImpl {
    // 直接注入
    @Autowired
    private RagUtils ragUtils;
    // 注入你上面定义的模型
    @Autowired
    private DeepSeekChatModel deepSeekChatModel;
    // 直接注入
    @Autowired
    private DeepSeekService deepSeekService;
    // RAG测试方法
    public void ragTest(String query) {
        // 1. 执行RAG查询
        log.info("执行查询: {}", query);
        // 构建RAG增强提示
        DSUserReqMessage ragPrompt = ragUtils.buildRagPrompt(query,"third");
        // 构建请求
        DeepSeekRequest request = new DeepSeekRequest();
        request.setDeepSeekModel(deepSeekChatModel);
        request.setMessages(Arrays.asList(ragPrompt));
        DSResponse dsResponse = deepSeekService.sendMessage(request);
        // 2. 输出结果
        log.info("\n输出结果:\n{}\n", dsResponse.getChoices().get(0).getMessage().getContent());
        log.info("=== RAG示例结束 ===");
    }
}
