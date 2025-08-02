package top.xiaoyu.deepseekdemo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import top.aoyudi.rag.DocumentLoader;
import top.aoyudi.rag.VectorStore;
import top.aoyudi.rag.properties.Document;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.List;

/**
 * 文档向量库配置
 */
@Configuration
public class RagVectorStoreConfig {
    @Autowired
    private VectorStore vectorStore;
    @Autowired
    private DocumentLoader documentLoader;

    @PostConstruct
    public void init() throws IOException {
        // 加载文件
        List<Document> documents = documentLoader.loadDocumentsFromPath("/Users/xiaoyu/Documents/advice_doc");
        // 向量库添加——自动向量化
        vectorStore.addDocuments("first",documents);
    }
}
