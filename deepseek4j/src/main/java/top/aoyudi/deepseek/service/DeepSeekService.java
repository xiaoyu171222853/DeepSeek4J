package top.aoyudi.deepseek.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import top.aoyudi.deepseek.entity.request.DeepSeekRequest;
import top.aoyudi.deepseek.entity.request.FinallyRequestBody;
import top.aoyudi.deepseek.entity.response.DSResponse;
import top.aoyudi.deepseek.entity.enums.ErrorCodeEnum;
import top.aoyudi.starter.DeepSeekConfig;
import top.aoyudi.starter.DeepSeekProperties;

/**
 * DeepSeek 服务类
 * 用于与 DeepSeek API 进行交互
 */
@Component
public class DeepSeekService {

    @Autowired
    private DeepSeekConfig deepSeekConfig;

    private static final Logger logger = LoggerFactory.getLogger(DeepSeekService.class);

    private final DeepSeekProperties properties;
    private final RestTemplate restTemplate;

    /**
     * 构造函数，初始化 DeepSeekProperties 和 RestTemplate
     */
    public DeepSeekService(DeepSeekProperties properties) {
        this.properties = properties;
        this.restTemplate =new RestTemplate();
    }

    /**
     * 发送消息到 DeepSeek API，并返回响应
     *
     * @param request DeepSeek 请求对象
     * @return DeepSeek 的响应
     */
    public DSResponse sendMessage(DeepSeekRequest request) {
        HttpHeaders headers = deepSeekConfig.createHeaders(); // 创建请求头
        // 构建请求体
        FinallyRequestBody finallyRequestBody = new FinallyRequestBody(request);
        
        HttpEntity<FinallyRequestBody> entity = new HttpEntity<>(finallyRequestBody, headers);

        logger.info("Sending request to DeepSeek API...");
        // 发送 HTTP POST 请求到 DeepSeek API
        ResponseEntity<DSResponse> response = restTemplate.exchange(
                properties.getApiUrl(),
                HttpMethod.POST,
                entity,
                DSResponse.class
        );

        return handleResponse(response); // 处理响应
    }

    // 处理响应，根据状态码判断请求是否成功
    private DSResponse handleResponse(ResponseEntity<DSResponse> response) {

        if (response.getStatusCodeValue()==200) {  // 适配 Spring Boot 3
            logger.info("FinallyRequestBody successful.");
            return response.getBody();
        } else {
            int statusCode = response.getStatusCode().value();  // 兼容 Spring Boot 3
            ErrorCodeEnum errorCode = ErrorCodeEnum.fromCode(statusCode);
            String errorMessage = (errorCode != null) ? errorCode.getDescription() + ": " + errorCode.getReason() : "未知错误：请联系支持";
            logger.error("FinallyRequestBody failed with error: {}", errorMessage);
            throw new RuntimeException(errorMessage);
        }
    }
}