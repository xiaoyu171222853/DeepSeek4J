package top.aoyudi.starter;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;


/**
 * DeepSeek 服务类
 * 负责提供与api交互的配置
 */
@Configuration
public class DeepSeekConfig {

    private final DeepSeekProperties properties;

    /**
     * 构造函数，初始化 DeepSeekProperties 和 RestTemplate
     * @param properties 实体
     */
    public DeepSeekConfig(DeepSeekProperties properties) {
        this.properties = properties;
    }

    /**
     * 创建请求头，设置授权和内容类型
     * @return HttpHeaders 请求头
     */
    public HttpHeaders createHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + properties.getApiKey());
        headers.set("Content-Type", "application/json");
        return headers;
    }
}