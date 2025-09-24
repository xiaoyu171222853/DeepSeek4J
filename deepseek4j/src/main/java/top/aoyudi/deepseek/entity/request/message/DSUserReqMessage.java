package top.aoyudi.deepseek.entity.request.message;

import lombok.*;
import lombok.experimental.SuperBuilder;
import top.aoyudi.chat.entity.ChatMessage;
import top.aoyudi.deepseek.entity.enums.DeepSeekRoleEnum;
/**
 * DeepSeek 用户请求消息 消息
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class DSUserReqMessage extends ChatMessage {

    @Builder.Default
    private String role = DeepSeekRoleEnum.USER.getRole();
    @Builder.Default
    private String name = DeepSeekRoleEnum.USER.getRole();
}
