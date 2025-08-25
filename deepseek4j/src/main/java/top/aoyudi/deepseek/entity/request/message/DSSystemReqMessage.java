package top.aoyudi.deepseek.entity.request.message;

import lombok.*;
import lombok.experimental.SuperBuilder;
import top.aoyudi.chat.entity.ChatMessage;
import top.aoyudi.deepseek.entity.enums.DeepSeekRoleEnum;

/**
 * DeepSeek 系统提示请求 消息
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class DSSystemReqMessage extends ChatMessage {

    @Builder.Default
    private String role = DeepSeekRoleEnum.SYSTEM.getRole();
    @Builder.Default
    private String name = DeepSeekRoleEnum.SYSTEM.getRole();
}
