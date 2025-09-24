package top.aoyudi.starter;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import top.aoyudi.deepseek.service.DeepSeekBalanceService;
import top.aoyudi.deepseek.service.DeepSeekService;
import top.aoyudi.deepseek.service.DeepSeekStreamService;

/**
 * DeepSeek 自动配置类
 * 当 DeepSeekService 类存在时，自动配置生效
 */
@Configuration
@ComponentScan({"top.aoyudi"})
@ConditionalOnClass(DeepSeekConfig.class)
@EnableConfigurationProperties(DeepSeekProperties.class)
public class DeepSeekAutoConfiguration {

    /**
     * 创建 DeepSeekService Bean
     * 如果没有其他 DeepSeekService Bean 存在，则创建此 Bean
     * @param properties 实体
     * @return DeepSeekConfig deepseek配置
     */
    @Bean
    @ConditionalOnMissingBean
    public DeepSeekConfig deepSeekConfig(DeepSeekProperties properties) {
        return new DeepSeekConfig(properties);
    }

    /**
     * Chat服务实例注册
     * @param properties DeepSeek 自动配置实体
     * @return DeepSeek 服务类  用于与 DeepSeek API 进行交互
     */
    @Bean
    @ConditionalOnMissingBean
    public DeepSeekService deepSeekService(DeepSeekProperties properties) {
        return new DeepSeekService(properties);
    }

    /**
     * Stream流式服务注册
     * @param properties DeepSeek 自动配置实体
     * @return DeepSeek 流服务类用于处理流式输出 服务
     */
    @Bean
    @ConditionalOnMissingBean
    public DeepSeekStreamService deepSeekStreamService(DeepSeekProperties properties) {
        return new DeepSeekStreamService(properties);
    }

    /**
     * 余额查询服务
     * @param properties DeepSeek 自动配置实体
     * @return 余额查询服务
     */
    @Bean
    @ConditionalOnMissingBean
    public DeepSeekBalanceService deepSeekBalanceService(DeepSeekProperties properties) {
        return new DeepSeekBalanceService(properties);
    }
}