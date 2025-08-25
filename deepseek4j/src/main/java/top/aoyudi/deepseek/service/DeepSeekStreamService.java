package top.aoyudi.deepseek.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import reactor.core.publisher.Flux;
import top.aoyudi.deepseek.entity.request.DeepSeekRequest;
import top.aoyudi.deepseek.entity.request.FinallyRequestBody;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import com.fasterxml.jackson.databind.ObjectMapper;
import top.aoyudi.deepseek.entity.response.DSResponse;
import top.aoyudi.starter.DeepSeekConfig;
import top.aoyudi.starter.DeepSeekProperties;

/**
 * DeepSeek 流服务类
 * 用于处理流式输出
 */
@Component
public class DeepSeekStreamService {

    private final DeepSeekProperties properties;
    private final RestTemplate restTemplate;
    private static final Logger logger = LoggerFactory.getLogger(DeepSeekStreamService.class);
    @Autowired
    private DeepSeekConfig deepSeekConfig;
    /**
     * 构造函数，初始化 DeepSeekProperties 和 RestTemplate
     *
     * @param properties DeepSeek 配置属性
     */
    public DeepSeekStreamService(DeepSeekProperties properties) {
        this.properties = properties;
        this.restTemplate =new RestTemplate();
    }

   /**
     * 发送消息到 DeepSeek API，并返回响应
     *
     * @param request DeepSeek 请求对象
     *
     * @return {@code Flux<top.aoyudi.deepseek.entity.response.DSResponse>} 回复体解析
     *
     */
   public Flux<DSResponse> sendMessageWithStream(DeepSeekRequest request) {
       HttpHeaders headers = deepSeekConfig.createHeaders(); // 创建请求头

       // 检查请求对象是否为空
       if (request == null) {
           throw new IllegalArgumentException("FinallyRequestBody cannot be null");
       }
       // 组织请求体
       FinallyRequestBody finallyRequestBody = new FinallyRequestBody(request);
       logger.info("Sending request to DeepSeek API...");
       return Flux.create(sink -> {
           // 使用 RestTemplate 执行 HTTP POST 请求，并处理流式响应
           restTemplate.execute(
               properties.getApiUrl(),
               HttpMethod.POST,
               requestCallback -> {
                   requestCallback.getHeaders().addAll(headers);
                   // 设置请求体
                   ObjectMapper objectMapper = new ObjectMapper();
                   try {
                       String requestBody = objectMapper.writeValueAsString(finallyRequestBody);
                       requestCallback.getBody().write(requestBody.getBytes());
                   } catch (Exception e) {
                       e.printStackTrace();
                   }
               },
               responseExtractor -> {
                   // 处理流式响应
                   try (BufferedReader reader = new BufferedReader(new InputStreamReader(responseExtractor.getBody()))) {
                       String line;
                       while ((line = reader.readLine()) != null) {
                           // 将每行数据推送到 Flux
                           if (!line.isBlank()) { // 避免推送空白行
                               DSResponse dsResponse = parseLineToDSResponse(jsonProcessor(line));
                               if (dsResponse != null) {
                                   sink.next(dsResponse);
                               }
                           }
                       }
                       sink.complete(); // 读取完成后，标记流结束
                   } catch (Exception e) {
                       sink.error(e); // 如果有异常，传递错误信号
                   }
                   return null;
               }
           );
       });
   }
    /**
     * 解析每一行数据为 DSResponse 对象
     */
    private DSResponse parseLineToDSResponse(String line) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(line, DSResponse.class);
        } catch (Exception e) {
            return null; // 返回一个空对象，或者根据业务逻辑处理
        }
    }

    private String jsonProcessor(String jsonString) throws JsonProcessingException {
        // **1. 过滤掉 keep-alive**
        if (jsonString.contains("keep-alive")|| jsonString.contains("DONE")) {
            System.out.println("Received keep-alive message, ignoring...");
            return null; // 直接丢弃，不处理
        }
        // **1. 去掉 data: 前缀**
        if (jsonString.startsWith("data: ")) {
            jsonString = jsonString.substring(6).trim(); // 去掉"data: "及多余空格
        }

        // **2. 解析 JSON**
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(jsonString);

        // **4. 转换回 JSON 字符串**
        return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(rootNode);
    }
}