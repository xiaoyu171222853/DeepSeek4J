package top.aoyudi.deepseek.entity.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * DeepSeek 响应体
 * {"id":"dab94aa4-ebb8-4689-bc60-154ae60cd097",
 * "object":"chat.completion.chunk",
 * "created":1741797681,
 * "model":"deepseek-reasoner",
 * "system_fingerprint":"",
 * "choices":[],
 * "usage":{}
 * }"
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DSResponse {
    /**
     * 响应的唯一标识符
     */
    private String id;

    /**
     * 响应的类型
     */
    private String object;

    /**
     * 响应创建的时间戳
     */
    private long created;

    /**
     * 使用的模型名称
     */
    private String model;

    /**
     * 此指纹表示模型运行时使用的后端配置。
     */
    private String system_fingerprint;

    /**
     * 选择列表
     */
    private List<DSRespChoice> choices;

    /**
     * 使用情况信息
     */
    private DSRespUsage usage;

    /**
     *  添加获取 choices 数量的方法
     */
    public int getChoicesCount() {
        return choices != null ? choices.size() : 0;
    }
}
