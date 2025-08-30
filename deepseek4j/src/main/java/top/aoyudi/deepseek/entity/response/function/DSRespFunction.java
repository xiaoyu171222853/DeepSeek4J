package top.aoyudi.deepseek.entity.response.function;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 返回————DeepSeek模型调用的 function
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DSRespFunction {
    /**
     * 模型调用的 function 名
     */
    public String name;
    /**
     * 要调用的 function 的参数，由模型生成，格式为 JSON。
     */
    public String arguments;
}
