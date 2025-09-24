package top.aoyudi.rag;

import top.aoyudi.rag.properties.Document;

import java.io.IOException;
import java.util.List;

/**
 * 文档加载器接口，定义从不同来源加载文档的方法
 */
public interface DocumentLoader {
    /**
     * 加载单个文档
     * @param source 文档来源（文件路径、URL等）
     * @return 加载的文档
     */
    Document loadDocument(String source);

    /**
     * 批量加载文档
     * @param sources 文档来源列表
     * @return 加载的文档列表
     */
    List<Document> loadDocuments(List<String> sources);

    /**
     * 加载指定路径下的所有符合条件的文档
     * @param path 文档所在路径
     * @return 加载的文档列表
     */
    List<Document> loadDocumentsFromPath(String path) throws IOException;
}