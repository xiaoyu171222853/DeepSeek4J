package top.aoyudi.deepseek.entity.response.function;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 返回————模型生成的 tool 调用，例如 function 调用。
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DSRespToolCalls {
    /**
     * tool 调用的 ID。
     */
    public String id;
    /**
     * tool 的类型。目前仅支持 function。
     */
    public String type;
    /**
     * 模型调用的 function。
     */
    public DSRespFunction function;
}
