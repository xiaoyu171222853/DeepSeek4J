package top.aoyudi.deepseek.entity.model;

import lombok.*;
import lombok.experimental.SuperBuilder;
import top.aoyudi.deepseek.entity.enums.DeepSeekModelEnum;
/**
 * DeepSeek 带有思考的对话模型——自己定义并配置Bean
 */
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder(toBuilder=true)
@AllArgsConstructor
@NoArgsConstructor
public class DeepSeekReasonerModel extends DeepSeekModel{
    // 模型名 ID
    @Builder.Default
    private String model = DeepSeekModelEnum.REASONER.getModelName();
    // 是否流式输出
    private boolean stream;
}
