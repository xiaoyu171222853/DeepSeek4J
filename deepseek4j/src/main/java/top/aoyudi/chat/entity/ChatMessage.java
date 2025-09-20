package top.aoyudi.chat.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * 消息底层实体 可用于存储记忆
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class ChatMessage {
    /**
     * 消息的角色，例如 "user" 或 "assistant" 等
     */
    @JsonProperty("role")
    public String role;

    /**
     * 消息的内容
     */
    @JsonProperty("content")
    public String content;
}
