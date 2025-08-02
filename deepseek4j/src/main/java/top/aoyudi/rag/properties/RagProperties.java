package top.aoyudi.rag.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import top.aoyudi.rag.impl.RecursiveCharacterTextSplitter;

/**
 * RAG功能配置属性类，绑定应用配置文件中的RAG相关参数
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "deepseek.rag")
public class RagProperties {

    /**
     * 文本块大小（字符数）
     */
    private int chunkSize = RecursiveCharacterTextSplitter.DEFAULT_CHUNK_SIZE;

    /**
     * 文本块重叠大小（字符数）
     */
    private int chunkOverlap = RecursiveCharacterTextSplitter.DEFAULT_CHUNK_OVERLAP;

}