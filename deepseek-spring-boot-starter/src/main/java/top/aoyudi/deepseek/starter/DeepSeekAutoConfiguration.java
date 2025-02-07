package top.aoyudi.deepseek.starter;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * DeepSeek 自动配置类
 * 当 DeepSeekService 类存在时，自动配置生效
 */
@Configuration
@ConditionalOnClass(DeepSeekConfig.class)
@EnableConfigurationProperties(DeepSeekProperties.class)
public class DeepSeekAutoConfiguration {

    /**
     * 创建 DeepSeekService Bean
     * 如果没有其他 DeepSeekService Bean 存在，则创建此 Bean
     */
    @Bean
    @ConditionalOnMissingBean
    public DeepSeekConfig deepSeekConfig(DeepSeekProperties properties) {
        return new DeepSeekConfig(properties);
    }
}