package top.xiaoyu.deepseekdemo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import top.aoyudi.deepseek.entity.model.DeepSeekChatModel;
import top.aoyudi.deepseek.entity.model.DeepSeekReasonerModel;

import java.util.Arrays;

/**
 * 配置在项目中使用的DeepSeek模型
 */
@Configuration
public class DeepSeekModelConfig {

    /**
     * 无思考 非流式 聊天（调用工具）
     * @return DeepSeekChatModel
     */
    @Bean
    public DeepSeekChatModel utilExecuteChatModel(){
        return DeepSeekChatModel.builder()
                .stream(false)  // 是否流式输出
                .function_name_list(Arrays.asList("getWeather","getCurrentDateTime")) // 调用本地工具
                .tool_choice("auto") // 工具选择
                .build();
    }

    /**
     * 无思考 非流式 聊天
     * @return DeepSeekChatModel
     */
    @Bean
    public DeepSeekChatModel deepSeekChatModel() {
        return DeepSeekChatModel.builder()
                .stream(false)
                .build();
    }

    /**
     * 有思考 非流式 聊天
     * @return DeepSeekReasonerModel
     */
    @Bean
    public DeepSeekReasonerModel deepSeekReasonerModel(){
        return DeepSeekReasonerModel.builder()
                .stream(false)
                .build();
    }

    /**
     * 无思考 流式 聊天
     * @return DeepSeekChatModel
     */
    @Bean
    public DeepSeekChatModel deepSeekChatByStreamModel(){
        return DeepSeekChatModel.builder()
                .stream(true)
                .build();
    }

    /**
     * 思考 流式 聊天
     * @return DeepSeekReasonerModel
     */
    @Bean
    public DeepSeekReasonerModel deepSeekReasonerByStreamModel(){
        return DeepSeekReasonerModel.builder()
                .stream(true)
                .build();
    }
}
