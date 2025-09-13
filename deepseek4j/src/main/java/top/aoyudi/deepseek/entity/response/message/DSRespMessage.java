package top.aoyudi.deepseek.entity.response.message;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import top.aoyudi.chat.entity.ChatMessage;
import top.aoyudi.deepseek.entity.response.function.DSRespToolCalls;

import java.util.List;

/**
 * 响应实体
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DSRespMessage extends ChatMessage {
    /**
     * 仅适用于 deepseek-reasoner 模型
     * reasoning_content
     */
    private String reasoning_content;

    /**
     * 模型生成的 tool 调用，例如 function 调用。
     */
    private List<DSRespToolCalls> tool_calls;

    /**
     * 回复内容
     */
    private String content;
}