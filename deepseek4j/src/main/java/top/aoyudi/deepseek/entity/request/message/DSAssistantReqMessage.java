package top.aoyudi.deepseek.entity.request.message;

import lombok.*;
import lombok.experimental.SuperBuilder;
import top.aoyudi.chat.entity.ChatMessage;
import top.aoyudi.deepseek.entity.enums.DeepSeekRoleEnum;

/**
 * DeepSeek AI助理请求 消息
 * 貌似用不到
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class DSAssistantReqMessage extends ChatMessage {

    @Builder.Default
    private String role = DeepSeekRoleEnum.ASSISTANT.getRole();
    @Builder.Default
    private String name = DeepSeekRoleEnum.ASSISTANT.getRole();
}
