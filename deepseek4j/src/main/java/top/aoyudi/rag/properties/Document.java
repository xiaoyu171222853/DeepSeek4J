package top.aoyudi.rag.properties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Map;

/**
 * 文档模型类，用于表示RAG系统中的文档数据
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Document {
    /**
     * 文档内容
     */
    private String content;

    /**
     * 文档元数据
     */
    private Map<String, Object> metadata;

    /**
     * 文档向量
     */
    private float[] vector;

    /**
     * 创建文档实例的便捷方法
     */
    public static Document of(String content) {
        return new Document(content, null, null);
    }

    /**
     * 创建带元数据的文档实例
     */
    public static Document of(String content, Map<String, Object> metadata) {
        return new Document(content, metadata, null);
    }
}