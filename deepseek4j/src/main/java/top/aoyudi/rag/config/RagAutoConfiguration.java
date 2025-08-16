package top.aoyudi.rag.config;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import top.aoyudi.rag.*;
import top.aoyudi.rag.impl.*;
import top.aoyudi.rag.util.RagUtils;
import top.aoyudi.rag.properties.RagProperties;

import javax.annotation.PostConstruct;


/**
 * RAG功能自动配置类，注册RAG相关组件Bean
 */
@Log4j2
@Configuration
@ConditionalOnClass(RagProperties.class)
@EnableConfigurationProperties(RagProperties.class)
public class RagAutoConfiguration {
    @Autowired
    private RagProperties ragProperties;

    @Bean
    @ConditionalOnMissingBean
    public TextSplitter textSplitter() {
        log.info("Configuring RecursiveCharacterTextSplitter with chunk size: {} and overlap: {}",
                ragProperties.getChunkSize(), ragProperties.getChunkOverlap());
        return new RecursiveCharacterTextSplitter(
                ragProperties.getChunkSize(),
                ragProperties.getChunkOverlap(),
                RecursiveCharacterTextSplitter.DEFAULT_SEPARATORS
        );
    }

    @Bean
    @ConditionalOnMissingBean
    public VectorSimilarityCalc vectorSimilarityCalc() {
        return new VectorSimilarityCalcImpl();
    }

    @Bean
    @ConditionalOnMissingBean
    public EmbeddingGenerator embeddingGenerator() {
        return new DocumentEmbeddingGenerator();
    }

    @Bean
    @ConditionalOnMissingBean
    public DocumentLoader fileSystemDocumentLoader() {
        return new FileSystemDocumentLoader();
    }


    @Bean
    @ConditionalOnMissingBean
    public VectorStore vectorStore(EmbeddingGenerator embeddingGenerator,
                                   TextSplitter textSplitter,VectorSimilarityCalc vectorSimilarityCalc) {
        return new InMemoryVectorStore(embeddingGenerator,textSplitter,vectorSimilarityCalc);
    }

    @Bean
    @ConditionalOnMissingBean
    public RagUtils ragUtils(VectorStore vectorStore) {
        return new RagUtils(vectorStore);
    }
}