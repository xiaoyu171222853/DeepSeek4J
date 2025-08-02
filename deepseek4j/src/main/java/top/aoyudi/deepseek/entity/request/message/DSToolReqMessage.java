package top.aoyudi.deepseek.entity.request.message;

import lombok.*;
import lombok.experimental.SuperBuilder;
import top.aoyudi.chat.entity.ChatMessage;
import top.aoyudi.deepseek.entity.enums.DeepSeekRoleEnum;

/**
 * DeepSeek 工具调用请求 消息
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class DSToolReqMessage extends ChatMessage {

    @Builder.Default
    private String role = DeepSeekRoleEnum.TOOL.getRole();

    private String type = "function";

    private String tool_call_id;
}
