package top.aoyudi.rag.impl;

import top.aoyudi.rag.properties.Document;
import top.aoyudi.rag.TextSplitter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 递归字符文本分割器，按自然边界分割文本为指定大小的块
 */
public class RecursiveCharacterTextSplitter implements TextSplitter {

    /**
     * 默认块大小（字符数）
     */
    public static final int DEFAULT_CHUNK_SIZE = 1000;

    /**
     * 默认块重叠（字符数）
     */
    public static final int DEFAULT_CHUNK_OVERLAP = 200;

    /**
     * 默认分隔符列表（按优先级排序）
     */
    public static final List<String> DEFAULT_SEPARATORS = List.of(
            "\n\n", "\n", ". ", ", ", " ", ""
    );

    private final int chunkSize;
    private final int chunkOverlap;
    private final List<String> separators;

    /**
     * 使用默认参数创建文本分割器
     */
    public RecursiveCharacterTextSplitter() {
        this(DEFAULT_CHUNK_SIZE, DEFAULT_CHUNK_OVERLAP, DEFAULT_SEPARATORS);
    }

    /**
     * 创建自定义参数的文本分割器
     */
    public RecursiveCharacterTextSplitter(int chunkSize, int chunkOverlap, List<String> separators) {
        if (chunkOverlap >= chunkSize) {
            throw new IllegalArgumentException("Chunk overlap must be less than chunk size");
        }
        this.chunkSize = chunkSize;
        this.chunkOverlap = chunkOverlap;
        this.separators = new ArrayList<>(separators);
    }

    @Override
    public List<Document> splitDocument(Document document) {
        return splitTextWithMetadata(document.getContent(), document.getMetadata());
    }

    @Override
    public List<String> splitText(String text) {
        return splitText(text, chunkSize, chunkOverlap, separators);
    }

    public List<Document> splitTextWithMetadata(String text, Map<String, Object> metadata) {
        List<String> chunks = splitText(text);
        return chunks.stream()
                .map(chunk -> Document.of(chunk, metadata))
                .collect(Collectors.toList());
    }

    /**
     * 递归分割文本为指定大小的块
     */
    private List<String> splitText(String text, int chunkSize, int chunkOverlap, List<String> separators) {
        List<String> finalChunks = new ArrayList<>();
        String separator = findSeparator(text, separators);

        if (separator.isEmpty()) {
            // 如果没有找到分隔符，直接按字符数分割
            for (int i = 0; i < text.length(); i += chunkSize - chunkOverlap) {
                int end = Math.min(i + chunkSize, text.length());
                finalChunks.add(text.substring(i, end));
            }
            return finalChunks;
        }

        // 使用找到的分隔符分割文本
        String[] splits = text.split(separator);
        List<String> goodSplits = new ArrayList<>();

        for (String split : splits) {
            if (split.length() < chunkSize) {
                goodSplits.add(split);
            } else {
                // 递归分割大块文本
                List<String> recursiveSplits = splitText(split, chunkSize, chunkOverlap, separators.subList(1, separators.size()));
                goodSplits.addAll(recursiveSplits);
            }
        }

        // 合并分割结果，处理重叠
        return mergeSplits(goodSplits, separator);
    }

    /**
     * 查找文本中可用的最高优先级分隔符
     */
    private String findSeparator(String text, List<String> separators) {
        for (String separator : separators) {
            if (text.contains(separator) && !separator.isEmpty()) {
                return separator;
            }
        }
        return separators.get(separators.size() - 1); // 返回最后一个分隔符（通常是空字符串）
    }

    /**
     * 合并分割结果并添加重叠
     */
    private List<String> mergeSplits(List<String> splits, String separator) {
        List<String> merged = new ArrayList<>();
        StringBuilder currentChunk = new StringBuilder();

        for (String split : splits) {
            String separatorToAdd = splits.indexOf(split) > 0 ? separator : "";
            String potentialChunk = currentChunk.length() > 0 ? 
                    currentChunk + separatorToAdd + split : split;

            if (potentialChunk.length() <= chunkSize) {
                currentChunk.append(separatorToAdd).append(split);
            } else {
                if (currentChunk.length() > 0) {
                    merged.add(currentChunk.toString());
                    // 添加重叠
                    currentChunk = new StringBuilder();
                    // 从当前split中截取与前一个块重叠的部分
                    int overlapStart = Math.max(0, split.length() - chunkOverlap);
                    currentChunk.append(split.substring(overlapStart));
                } else {
                    // 如果单个split就超过chunkSize，直接添加
                    merged.add(split);
                }
            }
        }

        // 添加最后一个块
        if (currentChunk.length() > 0) {
            merged.add(currentChunk.toString());
        }

        return merged;
    }
}