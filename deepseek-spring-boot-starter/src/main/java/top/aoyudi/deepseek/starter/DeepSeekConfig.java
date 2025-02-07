package top.aoyudi.deepseek.starter;

import org.springframework.http.HttpHeaders;
import org.springframework.context.annotation.Configuration;



/**
 * DeepSeek 服务类
 * 负责提供与api交互的配置
 */
@Configuration
public class DeepSeekConfig {

    private final DeepSeekProperties properties;

    /**
     * 构造函数，初始化 DeepSeekProperties 和 RestTemplate
     */
    public DeepSeekConfig(DeepSeekProperties properties) {
        this.properties = properties;
    }

    /**
     * 创建请求头，设置授权和内容类型
     */
    private HttpHeaders createHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + properties.getApiKey());
        headers.set("Content-Type", "application/json");
        return headers;
    }
}