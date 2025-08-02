package top.aoyudi.deepseek.entity.request.tool;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * function 的输入参数
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DSReqToolParameters {
    /**
     * 工具类型
     */
    @JsonProperty("type")
    private final String type = "object";  // 固定值

    /**
     * 实体
     */
    @JsonProperty("properties")
    private Map<String, Property> properties = new HashMap<>();

    /**
     * 是否必须
     */
    @JsonProperty("required")
    private List<String> required = new ArrayList<>();

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Property {
        /**
         * 参数类型
         */
        @JsonProperty("type")
        private String type;  // string/number/boolean等
        /**
         * 参数的描述
         */
        @JsonProperty("description")
        private String description;
    }
}
