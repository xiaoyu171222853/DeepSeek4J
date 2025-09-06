package top.xiaoyu.deepseekdemo.service.impl;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import top.aoyudi.chat.entity.ChatMessage;
import top.aoyudi.deepseek.entity.model.DeepSeekChatModel;
import top.aoyudi.deepseek.entity.request.DeepSeekRequest;
import top.aoyudi.deepseek.entity.request.message.DSUserReqMessage;
import top.aoyudi.deepseek.entity.response.DSResponse;
import top.aoyudi.deepseek.entity.response.function.DSRespFunction;
import top.aoyudi.deepseek.entity.response.message.DSRespMessage;
import top.aoyudi.deepseek.handler.ToolExecutor;
import top.aoyudi.deepseek.service.DeepSeekService;
import top.xiaoyu.deepseekdemo.service.IUtilService;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Log4j2
@Service
public class UtilServiceImpl implements IUtilService {
    // 直接注入即可
    @Resource
    public DeepSeekService deepSeekService;
    // 直接注入即可
    @Resource
    private DeepSeekChatModel utilExecuteChatModel;

    // 消息列表
    private static final List<ChatMessage> messages = new ArrayList<>();

    @Override
    public String testTool(String content) throws Exception {
        // 创建用户消息
        DSUserReqMessage dsUserReqMessage = new DSUserReqMessage();
        dsUserReqMessage.setContent(content);
        messages.add(dsUserReqMessage);
        // 构建请求request
        DeepSeekRequest request = new DeepSeekRequest();
        request.setMessages(messages);
        request.setDeepSeekModel(utilExecuteChatModel);
        // 发起请求获取回复
        DSResponse dsResponse = deepSeekService.sendMessage(request);
        // 获取返回值
        DSRespMessage message = dsResponse.getChoices().get(0).getMessage();
        Object resp = "";
        // 判断是否返回的调用工具
        if (!message.getTool_calls().isEmpty()) {
            DSRespFunction function = message.getTool_calls().get(0).getFunction();
            resp=ToolExecutor.executeTool(function.getName(), function.getArguments());
        }else {
            // 获取回复
            resp=message.getContent();
        }
        return resp.toString();
    }
}
