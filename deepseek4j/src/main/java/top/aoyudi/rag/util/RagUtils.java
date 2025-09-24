package top.aoyudi.rag.util;

import lombok.extern.log4j.Log4j2;
import org.springframework.util.Assert;
import top.aoyudi.deepseek.entity.request.message.DSUserReqMessage;
import top.aoyudi.rag.VectorStore;
import top.aoyudi.rag.properties.Document;

import java.util.List;

/**
 * RAG服务类
 */
@Log4j2
public class RagUtils {

    private final VectorStore vectorStore;

    public RagUtils(VectorStore vectorStore) {
        Assert.notNull(vectorStore, "VectorStore must not be null");
        this.vectorStore = vectorStore;
    }

    /**
     * 构建RAG增强提示
     * @param question 问题
     * @param repositoryKey 仓库标识
     * @return DSUserReqMessage RagPrompt
     */
    public DSUserReqMessage buildRagPrompt(String question, String repositoryKey) {
        log.info("Answering question with RAG from repository {}: {}", repositoryKey, question);
        // 1. 从指定仓库检索相关文档块
        List<Document> relevantDocs = vectorStore.similaritySearch(repositoryKey, question);
        // 2. 构建增强提示
        StringBuilder prompt = new StringBuilder();
        prompt.append("Use the following context to answer the question. If you can't find the answer in the context, say you don't know.\n\n");
        prompt.append("Context:\n");

        for (int i = 0; i < relevantDocs.size(); i++) {
            Document doc = relevantDocs.get(i);
            prompt.append("[")
                    .append(i + 1)
                    .append("] ")
                    .append(doc.getContent())
                    .append("\n");
        }

        prompt.append("\nQuestion: ").append(question);
        prompt.append("\nAnswer: ");

        return DSUserReqMessage.builder()
                .content(prompt.toString())
                .build();
    }
}