package top.aoyudi.rag.impl;

import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import top.aoyudi.rag.EmbeddingGenerator;
import top.aoyudi.rag.TextSplitter;
import top.aoyudi.rag.VectorSimilarityCalc;
import top.aoyudi.rag.VectorStore;
import top.aoyudi.rag.properties.Document;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 内存实现的向量存储仓库
 */
@Log4j2
public class InMemoryVectorStore implements VectorStore {
    // 文档仓库
    private final Map<String, List<Document>> repositoryDocuments = new HashMap<>();
    // 向量生成器
    private final EmbeddingGenerator embeddingGenerator;
    // 向量相似度计算
    private final VectorSimilarityCalc vectorSimilarityCalc;

    // 文本分片
    private final TextSplitter textSplitter;

    public InMemoryVectorStore(EmbeddingGenerator embeddingGenerator,TextSplitter textSplitter, VectorSimilarityCalc vectorSimilarityCalc) {
        this.embeddingGenerator = embeddingGenerator;
        this.textSplitter = textSplitter;
        this.vectorSimilarityCalc = vectorSimilarityCalc;
    }

    @Override
    public void createRepository(String repositoryKey) {
        repositoryDocuments.putIfAbsent(repositoryKey, new ArrayList<>());
        log.info("Created or verified repository: {}", repositoryKey);
    }

    @Override
    public void deleteRepository(String repositoryKey) {
        repositoryDocuments.remove(repositoryKey);
        log.info("Deleted repository: {}", repositoryKey);
    }

    @Override
    public List<String> listRepositories() {
        return new ArrayList<>(repositoryDocuments.keySet());
    }

    @Override
    public void addDocument(String repositoryKey, Document document) {
        // 确保仓库存在
        createRepository(repositoryKey);
        // 转向量
        if (document.getVector() == null) {
            document.setVector(embeddingGenerator.generate(document.getContent()));
        }
        // 仓库添加
        repositoryDocuments.get(repositoryKey).add(document);
        log.info("Added document to repository {}. Total documents: {}", 
            repositoryKey, repositoryDocuments.get(repositoryKey).size());
    }

    @Override
    public void addDocuments(String repositoryKey, List<Document> documents) {
        log.info("Processing {} documents into repository {}...", documents.size(), repositoryKey);
        List<Document> chunks = documents.stream()
                .flatMap(doc -> textSplitter.splitDocument(doc).stream())
                .collect(Collectors.toList());

        log.info("Split {} documents into {} chunks", documents.size(), chunks.size());
        chunks.forEach(chunk -> this.addDocument(repositoryKey, chunk));
        log.info("Successfully added documents to repository {}", repositoryKey);
    }

    @Override
    public List<Document> similaritySearch(String repositoryKey, float[] queryVector) {
        List<Document> documents = repositoryDocuments.getOrDefault(repositoryKey, new ArrayList<>());
        return documents.stream()
                .map(doc -> new DocumentScore(doc, vectorSimilarityCalc.calcSimilarity(queryVector, doc.getVector())))
                .sorted((a, b) -> Float.compare(b.getScore(), a.getScore()))
                .map(DocumentScore::getDocument)
                .collect(Collectors.toList());
    }

    @Override
    public List<Document> similaritySearch(String repositoryKey, String queryText) {
        float[] queryVector = embeddingGenerator.generate(queryText);
        return similaritySearch(repositoryKey, queryVector);
    }

    @Override
    public void clear(String repositoryKey) {
        if (repositoryDocuments.containsKey(repositoryKey)) {
            repositoryDocuments.get(repositoryKey).clear();
            log.info("Cleared repository: {}", repositoryKey);
        } else {
            log.warn("Attempted to clear non-existent repository: {}", repositoryKey);
        }
    }

    /**
     * 辅助类用于存储文档和相似度分数
     */
    @Getter
    private static class DocumentScore {
        private final Document document;
        private final float score;

        public DocumentScore(Document document, float score) {
            this.document = document;
            this.score = score;
        }

    }
}