package top.xiaoyu.deepseekdemo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import top.aoyudi.rag.DocumentLoader;
import top.aoyudi.rag.VectorStore;
import top.aoyudi.rag.properties.Document;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.Arrays;
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
        // 加载文件  示例——实际开发根据自己需要，应避免路径硬编码
        List<Document> documents1 = documentLoader.loadDocumentsFromPath("/Users/xiaoyu/local/project/advice_doc");
        // 向量库添加——自动向量化
        vectorStore.addDocuments("first",documents1);
        // 多个指定文件
        List<Document> documents = documentLoader.loadDocuments(Arrays.asList("/Users/xiaoyu/local/project/advice_doc/中华人民共和国兵役法.docx","/Users/xiaoyu/local/project/advice_doc/征兵工作条例.docx"));
        vectorStore.addDocuments("second",documents);
        // 单个指定文件
        Document document = documentLoader.loadDocument("/Users/xiaoyu/local/project/advice_doc/中华人民共和国兵役法.docx");
        vectorStore.addDocument("third",document);
    }
}
