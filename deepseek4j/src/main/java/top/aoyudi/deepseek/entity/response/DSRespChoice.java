package top.aoyudi.deepseek.entity.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import top.aoyudi.deepseek.entity.response.message.DSRespMessage;

/**
 * DeepSeek 选择项
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DSRespChoice {
    /**
     * 非流式返回的消息对象
     */
    private DSRespMessage message;

    /**
     * 流式返回的消息对象
     */
    private DSRespMessage delta;

    /**
     * 完成原因
     */
    private String finish_reason;

    /**
     * 日志概率
     */
    private String logprobs;

    /**
     * 选择项的索引
     */
    private int index;
}
