package top.aoyudi.starter;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.ConstructorBinding;

/**
 * DeepSeek 自动配置实体
 */
@Data
@ConfigurationPropertiesScan // 确保被扫描到
@ConfigurationProperties(prefix = "deepseek")
@ConstructorBinding // Spring Boot 2.6+ 需要
public class DeepSeekProperties {
    /**
     * DeepSeek API Key
     */
    private String apiKey;
    /**
     * DeepSeek API URL
     */
    private String apiUrl = "https://api.deepseek.com/chat/completions"; // 默认值
    /**
     * DeepSeek 余额查询
     */
    private String balanceUrl = "https://api.deepseek.com/user/balance";
}