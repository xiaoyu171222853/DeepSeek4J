package top.aoyudi.rag;

import top.aoyudi.rag.properties.Document;

import java.util.List;
import java.util.Map;

/**
 * 文本分割器接口，定义文档分割功能
 */
public interface TextSplitter {
    /**
     * 将文档分割成块
     * @param document 文档对象
     * @return 分割后的文档块列表
     */
    List<Document> splitDocument(Document document);

    /**
     * 将文本分割成块
     * @param text 文本内容
     * @return 分割后的文本块列表
     */
    List<String> splitText(String text);

    /**
     * 将带元数据的文本分割成块
     * @param text 文本内容
     * @param metadata 元数据
     * @return 分割后的文档块列表
     */
    List<Document> splitTextWithMetadata(String text, Map<String, Object> metadata);
}