package top.aoyudi.deepseek.entity.request.tool;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
/**
 * DeepSeek 工具请求
 */
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DSReqTool {
    /**
     * 类型——目前官方仅支持function
     */
    @Builder.Default
    private String type = "function";
    /**
     * 要调用的工具
     */
    private DSReqFunction function;
}
