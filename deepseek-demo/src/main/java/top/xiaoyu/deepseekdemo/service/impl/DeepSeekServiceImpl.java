package top.xiaoyu.deepseekdemo.service.impl;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import top.aoyudi.chat.entity.ChatMessage;
import top.aoyudi.deepseek.entity.model.DeepSeekModel;
import top.aoyudi.deepseek.entity.request.DeepSeekRequest;
import top.aoyudi.deepseek.entity.request.message.DSUserReqMessage;
import top.aoyudi.deepseek.entity.response.DSResponse;
import top.aoyudi.deepseek.entity.response.message.DSRespMessage;
import top.aoyudi.deepseek.service.DeepSeekService;
import top.aoyudi.deepseek.service.DeepSeekStreamService;
import top.xiaoyu.deepseekdemo.service.IDeepSeekService;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class DeepSeekServiceImpl implements IDeepSeekService {

    // 直接注入即可
    @Resource
    private DeepSeekService deepSeekService;
    // 直接注入即可
    @Resource
    private DeepSeekStreamService deepSeekStreamService;

    // 请求体
    private static final DeepSeekRequest request = new DeepSeekRequest();
    // 消息列表
    private static final List<ChatMessage> messages = new ArrayList<>();

    @Override
    public CompletableFuture<DSRespMessage> chat(String content, DeepSeekModel deepSeekModel) {
        // 创建请求 message
        DSUserReqMessage dsUserReqMessage = DSUserReqMessage.builder()
                .content(content)
                .build();
        // 添加
        messages.add(dsUserReqMessage);
        // 构建请求
        request.setDeepSeekModel(deepSeekModel);
        request.setMessages(messages);
        // 调用方法 发送请求
        DSResponse dsResponse = deepSeekService.sendMessage(request);
        // 返回体 中获取 回复信息
        DSRespMessage dsRespMessage = dsResponse.getChoices().get(0).getMessage();
        // 转换为 chatMessage存在消息列表(记忆)
        ChatMessage chatMessage = new ChatMessage();
        BeanUtils.copyProperties(dsRespMessage, chatMessage);
        // 存储在消息列表
        messages.add(chatMessage);
        // 返回 返回体 中获取 的 回复信息
        return CompletableFuture.completedFuture(dsRespMessage);
    }

    @Override
    public Flux<DSResponse> chatByStream(String content, DeepSeekModel deepSeekModel) {
        // 创建请求 message
        DSUserReqMessage dsUserReqMessage = DSUserReqMessage.builder().build();
        dsUserReqMessage.setContent(content);
        // 添加
        messages.add(dsUserReqMessage);
        // 构建请求体
        request.setMessages(messages);
        request.setDeepSeekModel(deepSeekModel);
        // 判断 deepSeekModel 类型并作对应转换
        return deepSeekStreamService.sendMessageWithStream(request)
                .flatMap(Flux::just);
    }
}
