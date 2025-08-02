package top.aoyudi.deepseek.entity.request;

import lombok.Data;
import top.aoyudi.chat.entity.ChatMessage;
import top.aoyudi.deepseek.entity.model.DeepSeekModel;
import top.aoyudi.deepseek.entity.request.tool.DSReqTool;

import java.util.List;

/**
 * DeepSeek 请求体
 */
@Data
public class DeepSeekRequest {
    /**
     * 使用的模型
     */
    private DeepSeekModel deepSeekModel;

    /**
     * 消息列表
     */
    private List<ChatMessage> messages;

}

