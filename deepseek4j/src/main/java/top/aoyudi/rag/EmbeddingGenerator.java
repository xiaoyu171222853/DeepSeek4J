package top.aoyudi.rag;

/**
 * 嵌入生成器接口，定义文本转向量的功能
 */
public interface EmbeddingGenerator {
    /**
     * 将文本转换为向量表示
     * @param text 输入文本
     * @return 向量数组
     */
    float[] generate(String text);
}