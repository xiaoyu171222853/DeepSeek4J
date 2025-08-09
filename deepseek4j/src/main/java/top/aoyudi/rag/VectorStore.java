package top.aoyudi.rag;

import top.aoyudi.rag.properties.Document;

import java.util.List;

/**
 * 向量存储接口，定义向量的添加、查询和删除操作，支持多仓库（key-value形式）
 */
public interface VectorStore {
    /**
     * 创建新仓库（如果不存在）
     * @param repositoryKey 仓库标识
     */
    void createRepository(String repositoryKey);

    /**
     * 删除指定仓库及其所有文档
     * @param repositoryKey 仓库标识
     */
    void deleteRepository(String repositoryKey);

    /**
     * 列出所有现有仓库
     * @return 仓库标识列表
     */
    List<String> listRepositories();

    /**
     * 添加文档向量到指定仓库
     * @param repositoryKey 仓库标识
     * @param document 文档对象
     */
    void addDocument(String repositoryKey, Document document);

    /**
     * 将文档添加到指定向量仓库（会自动分割为块）
     * @param documents 文档列表
     * @param repositoryKey 目标仓库标识
     */
    void addDocuments(String repositoryKey, List<Document> documents);

    /**
     * 根据查询向量从指定仓库检索相似文档
     * @param repositoryKey 仓库标识
     * @param queryVector 查询向量
     * @return 相似文档列表
     */
    List<Document> similaritySearch(String repositoryKey, float[] queryVector);

    /**
     * 根据查询文本从指定仓库检索相似文档（内部会自动生成向量）
     * @param repositoryKey 仓库标识
     * @param queryText 查询文本
     * @return 相似文档列表
     */
    List<Document> similaritySearch(String repositoryKey, String queryText);

    /**
     * 清空指定仓库的所有文档
     * @param repositoryKey 仓库标识
     */
    void clear(String repositoryKey);
}