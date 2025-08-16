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
import top.aoyudi.deepseek.entity.enums.ErrorCodeEnum;
import top.aoyudi.deepseek.entity.response.balance.DSBalance;
import top.aoyudi.starter.DeepSeekConfig;
import top.aoyudi.starter.DeepSeekProperties;

/**
 * DeepSeek 查询余额服务
 */
@Component
public class DeepSeekBalanceService {

    private static final Logger logger = LoggerFactory.getLogger(DeepSeekService.class);
    @Autowired
    private DeepSeekConfig deepSeekConfig;

    private final DeepSeekProperties deepSeekProperties;
    private final RestTemplate restTemplate;

    public DeepSeekBalanceService(DeepSeekProperties deepSeekProperties) {
        this.deepSeekProperties = deepSeekProperties;
        this.restTemplate =new RestTemplate();
    }

    /**
     * 获取余额
     * @return 余额
     */
    public DSBalance getBalance() {
        // 创建请求头
        HttpHeaders headers = deepSeekConfig.createHeaders();
        HttpEntity<String> entity = new HttpEntity<>(headers);
        logger.info("Sending request to DeepSeek API...");
        // 发送请求
        ResponseEntity<DSBalance> exchange = restTemplate.exchange(
                deepSeekProperties.getBalanceUrl(),
                HttpMethod.GET,
                entity,
                DSBalance.class);
        return handleResponse(exchange);
    }

    // 处理响应，根据状态码判断请求是否成功
    private DSBalance handleResponse(ResponseEntity<DSBalance> response) {
        if (response.getStatusCodeValue()==200) {  // 适配 Spring Boot 3
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
