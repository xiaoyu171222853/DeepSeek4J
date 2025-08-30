package top.aoyudi.rag.impl;
import top.aoyudi.rag.EmbeddingGenerator;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class DocumentEmbeddingGenerator implements EmbeddingGenerator {
    private static final int EMBEDDING_DIMENSION = 128;
    private static final Pattern WORD_PATTERN = Pattern.compile("\\b[a-zA-Z0-9]+\\b");
    private static final int TOP_K_WORDS = 1000; // 用于构建词汇表的高频词数量

    // 词汇表：词 -> 索引映射
    private final Map<String, Integer> vocabulary;

    public DocumentEmbeddingGenerator() {
        // 初始化词汇表（实际应用中应从语料库训练得到）
        this.vocabulary = new HashMap<>();
    }

    @Override
    public float[] generate(String content) {
        // 2. 文本预处理
        List<String> words = preprocessText(content);

        // 3. 更新词汇表（首次使用或增量更新）
        updateVocabulary(words);

        // 4. 生成文档嵌入向量
        return generateEmbedding(words);
    }

    /**
     * 文本预处理：分词、小写转换、过滤
     */
    private List<String> preprocessText(String content) {
        List<String> words = new ArrayList<>();
        Scanner scanner = new Scanner(content);
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine().toLowerCase();
            // 提取单词
            var matcher = WORD_PATTERN.matcher(line);
            while (matcher.find()) {
                String word = matcher.group();
                if (word.length() > 1) { // 过滤太短的词
                    words.add(word);
                }
            }
        }
        scanner.close();
        return words;
    }

    /**
     * 更新词汇表，只保留高频词
     */
    private void updateVocabulary(List<String> words) {
        // 统计词频
        Map<String, Integer> wordCounts = new HashMap<>();
        for (String word : words) {
            wordCounts.put(word, wordCounts.getOrDefault(word, 0) + 1);
        }

        // 只保留高频词并更新词汇表
        List<Map.Entry<String, Integer>> sortedEntries = wordCounts.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .limit(TOP_K_WORDS)
                .collect(Collectors.toList());

        for (Map.Entry<String, Integer> entry : sortedEntries) {
            String word = entry.getKey();
            if (!vocabulary.containsKey(word)) {
                vocabulary.put(word, vocabulary.size() % EMBEDDING_DIMENSION);
            }
        }
    }

    /**
     * 基于词频和词汇表生成嵌入向量
     */
    private float[] generateEmbedding(List<String> words) {
        float[] embedding = new float[EMBEDDING_DIMENSION];
        Map<String, Integer> wordCounts = new HashMap<>();

        // 统计当前文档的词频
        for (String word : words) {
            wordCounts.put(word, wordCounts.getOrDefault(word, 0) + 1);
        }

        // 计算总词数
        int totalWords = words.size();
        if (totalWords == 0) {
            return embedding;
        }

        // 生成嵌入：基于词频和词汇表索引
        for (Map.Entry<String, Integer> entry : wordCounts.entrySet()) {
            String word = entry.getKey();
            int count = entry.getValue();
            if (vocabulary.containsKey(word)) {
                int index = vocabulary.get(word);
                // 归一化词频作为权重
                embedding[index] += (float) count / totalWords;
            }
        }

        // 归一化向量
        normalizeVector(embedding);

        return embedding;
    }

    /**
     * 归一化向量
     */
    private void normalizeVector(float[] vector) {
        float norm = 0.0f;
        for (float value : vector) {
            norm += value * value;
        }
        norm = (float) Math.sqrt(norm);
        if (norm > 0) {
            for (int i = 0; i < vector.length; i++) {
                vector[i] /= norm;
            }
        }
    }
}
