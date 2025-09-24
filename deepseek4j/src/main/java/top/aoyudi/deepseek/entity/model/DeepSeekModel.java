package top.aoyudi.deepseek.entity.model;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.context.annotation.Configuration;
import top.aoyudi.deepseek.entity.request.tool.DSReqTool;

import java.util.ArrayList;
import java.util.List;

/**
 * DeepSeek 底层模型——不直接使用
 */
@Data
@SuperBuilder(toBuilder=true)
@NoArgsConstructor
@AllArgsConstructor
@Configuration
public abstract class DeepSeekModel {
    /**
     * 介于 -2.0 和 2.0 之间的数字。
     * 如果该值为正，那么新 token 会根据其在已有文本中的出现频率受到相应的惩罚，
     * 降低模型重复相同内容的可能性。
     */
    @Builder.Default
    private double frequency_penalty = 0.0;
    /**
     * 介于 1 到 8192 间的整数，
     * 限制一次请求中模型生成 completion 的最大 token 数。
     * 输入 token 和输出 token 的总长度受模型的上下文长度的限制。
     * 如未指定 max_tokens参数，默认使用 4096。
     */
    @Builder.Default
    private int max_tokens = 4096;
    /**
     * 介于 -2.0 和 2.0 之间的数字。
     * 如果该值为正，那么新 token 会根据其是否已在已有文本中出现受到相应的惩罚，
     * 从而增加模型谈论新主题的可能性。
     */
    @Builder.Default
    private double presence_penalty = 0.0;
    /**
     *采样温度，介于 0 和 2 之间。更高的值，
     * 如 0.8，会使输出更随机，而更低的值，如 0.2，会使其更加集中和确定。
     * 我们通常建议可以更改这个值或者更改 top_p，但不建议同时对两者进行修改。
     */
    @Builder.Default
    private double temperature = 1.0;
    /**
     * 作为调节采样温度的替代方案，模型会考虑前 top_p 概率的 token 的结果。
     * 所以 0.1 就意味着只有包括在最高 10% 概率中的 token 会被考虑。
     * 我们通常建议修改这个值或者更改 temperature，但不建议同时对两者进行修改。
     */
    @Builder.Default
    private double top_p = 1.0;

    /**
     * 是否返回所输出 token 的对数概率。
     * 如果为 true，则在 message 的 content 中返回每个输出 token 的对数概率。
     */
    //private boolean logprobs;

    /**
     * 一个介于 0 到 20 之间的整数 N，
     * 指定每个输出位置返回输出概率 top N 的 token，
     * 且返回这些 token 的对数概率。
     * 指定此参数时，logprobs 必须为 true。
     */
    //private int top_logprobs;
    /**
     * 调用工具
     */
    @Builder.Default
    private List<String> function_name_list= new ArrayList<>();

    /**
     * 可以是 String 或 ToolChoice 对象
     */
    @Builder.Default
    private Object tool_choice="auto";
} 