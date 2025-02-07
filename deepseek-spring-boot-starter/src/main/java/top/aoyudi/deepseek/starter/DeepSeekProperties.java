package top.aoyudi.deepseek.starter;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "deepseek")
public class DeepSeekProperties {
    /**
     * DeepSeek API Key
     */
    private String apiKey;
    /**
     * DeepSeek API URL
     */
    private String apiUrl = "https://api.deepseek.com/chat/completions"; // 默认值
}