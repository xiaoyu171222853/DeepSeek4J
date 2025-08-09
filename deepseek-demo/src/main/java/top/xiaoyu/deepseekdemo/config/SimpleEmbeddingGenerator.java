package top.xiaoyu.deepseekdemo.config;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import top.aoyudi.rag.EmbeddingGenerator;

/**
 * 自定义嵌入生成器————使用简单哈希算法生成文本向量
 */
@Log4j2
@Component
public class SimpleEmbeddingGenerator implements EmbeddingGenerator {

    /**
     * 嵌入向量维度
     */
    private static final int EMBEDDING_DIMENSION = 128;

    @Override
    public float[] generate(String text) {
        log.info("Generating simple embedding for text (length: {})", text.length());
        float[] embedding = new float[EMBEDDING_DIMENSION];

        // 使用简单哈希算法生成向量
        int textHash = text.hashCode();
        for (int i = 0; i < EMBEDDING_DIMENSION; i++) {
            // 基于位置和文本哈希生成伪随机值
            long seed = (long) textHash * (i + 1);
            // 生成-1到1之间的随机浮点数
            embedding[i] = (float) ((seed % 2000) / 1000.0 - 1.0);
        }
        return embedding;
    }
}