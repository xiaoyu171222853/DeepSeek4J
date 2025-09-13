package top.aoyudi.deepseek.entity.request.tool;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DSReqToolChoice {
    private String type = "function";
    private Function function;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Function {
        private String name;
    }
}
