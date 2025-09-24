package top.aoyudi.deepseek.entity.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DeepSeek 使用情况
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DSRespUsage {
    /**
     * 提示令牌数
     */
    private long prompt_tokens;

    /**
     * 完成令牌数
     */
    private long completion_tokens;

    /**
     * 总令牌数
     */
    private long total_tokens;

    /**
     * 缓存的令牌数
     */
    private long cached_tokens;

    /**
     * 提示缓存命中令牌数
     */
    private long prompt_cache_hit_tokens;

    /**
     * 提示缓存未命中令牌数
     */
    private long prompt_cache_miss_tokens;
}


