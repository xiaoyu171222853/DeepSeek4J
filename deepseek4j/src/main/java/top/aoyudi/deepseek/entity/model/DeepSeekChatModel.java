package top.aoyudi.deepseek.entity.model;

import lombok.*;
import lombok.experimental.SuperBuilder;
import top.aoyudi.deepseek.entity.enums.DeepSeekModelEnum;

/**
 * DeepSeek 普通对话模型——自己定义并配置Bean
 */
@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder(toBuilder=true)
@AllArgsConstructor
@NoArgsConstructor
public class DeepSeekChatModel extends DeepSeekModel{
    // 模型名 ID
    @Builder.Default
    private String model = DeepSeekModelEnum.CHAT.getModelName();
    // 是否流式输出
    private boolean stream;
}
