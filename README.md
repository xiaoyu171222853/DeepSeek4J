# DeepSeek4J：让Java开发者轻松集成DeepSeek AI能力

## 🌟 什么是DeepSeek4J

DeepSeek4J是一个专为Java开发者打造的DeepSeek API集成工具，旨在简化Spring Boot应用中DeepSeek AI能力的配置和调用流程。无论是Spring Boot 2还是最新的Spring Boot 3，DeepSeek4J都能提供无缝支持，让你专注于业务逻辑而非API调用细节。

## 🚀 核心功能特性

### 1. Spring Boot原生支持
- 提供自动配置，开箱即用
- 支持Spring Boot 2.x和3.x全版本
- 简化的依赖注入模型

### 2. 强大的API封装
- 完整封装DeepSeek API调用逻辑
- 支持同步和异步请求
- 统一的请求/响应处理机制

### 3. 工具调用能力
- 注解驱动的工具函数定义
- 自动参数解析和类型转换
- 灵活的工具注册与执行机制

### 4. 高度可配置
- 通过application.yml/properties轻松配置
- 支持模型参数精细化调整
- 内置默认配置，降低使用门槛
### 5. 轻量化
### 6. 支持RAG

## 📦 项目结构

```
DeepSeek/
├── deepseek-demo/           # 使用示例及功能测试
└── deepseek4j/              # DeepSeek API调用核心封装
```
## RAG
`DocumentEmbeddingGenerator` 是一个文档嵌入向量生成器，能够将 TXT、Word（.docx）、Markdown（.md）等格式的文档转换为固定维度的数值向量，用于文档相似性计算或内容特征提取。
### 向量生成核心流程
#### 1. 文档文本提取
从不同类型文档中提取纯文本内容，作为向量生成的原始数据：

| 文件类型 | 提取方式 | 依赖库 |
|----------|----------|--------|
| TXT | 直接读取文件内容 | Java NIO |
| Word（.docx） | 通过 `XWPFWordExtractor` 提取 | Apache POI |
| Markdown（.md） | 解析并过滤格式符号，提取纯文本 | CommonMark |
#### 2. 文本预处理
对提取的文本进行标准化清洗，确保特征一致性：
预处理步骤：
    小写转换：消除大小写差异（"Hello" → "hello"）
    正则分词：使用 \b[a-zA-Z0-9]+\b 提取有效单词
    短词过滤：移除长度 ≤ 1 的无意义词汇
#### 3. 动态词汇表构建
维护词汇表映射（单词 → 向量维度索引），支撑特征映射：
词汇表特性：
    动态更新：随文档处理自动添加新词汇
    高频优先：仅保留词频最高的前 1000 个单词
    维度约束：索引值通过 % 128 映射到 128 维向量空间

#### 4. 向量生成与归一化
将文本特征转换为数值向量并标准化：
向量生成关键逻辑：
    权重计算：每个单词的贡献 = 词频 / 总词数（归一化词频）
    维度赋值：权重累加到词汇表对应的向量维度
    L2 归一化：将向量缩放至单位长度，消除文档长度影响

### 自定义向量生成器
在你自己项目任意文件夹写生成器eg：
```java
/**
 * 无模型嵌入生成器，使用简单哈希算法生成文本向量
 */
@Log4j2
@Component
public class SimpleEmbeddingGenerator implements EmbeddingGenerator {

    /**
     * 嵌入向量维度
     */
    private static final int EMBEDDING_DIMENSION = 128;

    @Override
    public float[] generate(String text) {
        log.info("Generating simple embedding for text (length: {})", text.length());
        float[] embedding = new float[EMBEDDING_DIMENSION];

        // 使用简单哈希算法生成向量
        int textHash = text.hashCode();
        for (int i = 0; i < EMBEDDING_DIMENSION; i++) {
            // 基于位置和文本哈希生成伪随机值
            long seed = (long) textHash * (i + 1);
            // 生成-1到1之间的随机浮点数
            embedding[i] = (float) ((seed % 2000) / 1000.0 - 1.0);
        }
        return embedding;
    }
}
```

### 向量相似度计算
默认采用余弦向量相似度，依旧可自定义

### 自定义向量相似度计算方法
在你自己项目任意文件夹自定义向量相似度计算方法 eg：
```java
@Component
public class YourSelfSimilarityCalcImpl implements VectorSimilarityCalc {
    @Override
    public float calcSimilarity(float[] vectorA, float[] vectorB) {
        float dotProduct = 0.0f;
        float normA = 0.0f;
        float normB = 0.0f;

        for (int i = 0; i < vectorA.length && i < vectorB.length; i++) {
            dotProduct += vectorA[i] * vectorB[i];
            normA += vectorA[i] * vectorA[i];
            normB += vectorB[i] * vectorB[i];
        }

        if (normA == 0 || normB == 0) {
            return 0.0f;
        }

        return dotProduct / (float) (Math.sqrt(normA) * Math.sqrt(normB));
    }
}
```

## 🔧 快速开始指南

### 1. 添加依赖

在你的Spring Boot项目pom.xml中添加以下依赖：

```xml
<dependency>
    <groupId>top.aoyudi.deepseek</groupId>
    <artifactId>deepseek4j</artifactId>
    <version>2.5.3</version> <!-- 使用最新版本 -->
</dependency>
```

### 2. 配置API密钥

在application.properties/yml中配置你的DeepSeek API密钥：

```properties
deepseek.api-key=sk-d3744xxxxxxxxxxxxxxxxxxx
```

### 3. 创建模型配置

```java
@Configuration
public class DeepSeekModelConfig {
    // 无思考非流式聊天
    @Bean
    public DeepSeekChatModel deepSeekChatModel() {
        return DeepSeekChatModel.builder()
                .stream(false)
                .build();
    }
    
    // 有思考流式聊天
    @Bean
    public DeepSeekReasonerModel deepSeekReasonerModel(){
        return DeepSeekReasonerModel.builder()
                .stream(true)
                .build();
    }
    // 其他模型配置
}
```

### 4. 调用AI服务

```java
@Service
public class AIService {
    // 直接注入
    @Resource
    private DeepSeekService deepSeekService;
    // 注入你上面定义的模型
    @Resource
    private DeepSeekChatModel deepSeekChatModel;
    
    private static final List<ChatMessage> messages = new ArrayList<>();
    
    public String chat(String content) {
        // 创建用户消息
        DSUserReqMessage userMessage = DSUserReqMessage.builder()
                .content(content)
                .build();
        messages.add(userMessage);
        
        // 构建请求
        DeepSeekRequest request = new DeepSeekRequest();
        request.setDeepSeekModel(deepSeekChatModel);
        request.setMessages(messages);
        
        // 发送请求并获取响应
        DSResponse response = deepSeekService.sendMessage(request);
        DSRespMessage dsRespMessage = response.getChoices().get(0).getMessage();

        // 转换为 chatMessage存在消息列表(记忆)
        ChatMessage aiMessage = new ChatMessage();
        BeanUtils.copyProperties(dsRespMessage, aiMessage);
        
        // 保存AI响应到对话历史
        messages.add(aiMessage);
        
        return aiMessage.getContent();
    }
}
```

## 🔌 工具调用示例

DeepSeek4J提供了强大的工具调用能力，只需简单注解即可让AI模型调用你的Java方法：

```java
@Component
public class TestTools {
    @ToolFunction(description = "获取当前时间")
    private String getCurrentDateTime() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return LocalDateTime.now().format(dtf);
    }
    
    @ToolFunction(description = "获取指定城市的天气信息")
    public String getWeather(
            @ToolParam(name = "location", type = "string", 
                      description = "城市名称，例如：北京、上海", required = true) String location,
            @ToolParam(name = "date", type = "string", 
                      description = "日期，例如2025年1月5日") String date) {
        // 实际项目中这里会调用真实的天气API
        return location + "在" + date + "的天气晴朗，气温25°C";
    }
}
```

```java
@Configuration
public class DeepSeekModelConfig {

    /**
     * 无思考 非流式 聊天（调用工具）
     * @return DeepSeekChatModel
     */
    @Bean
    public DeepSeekChatModel utilExecuteChatModel() {
        return DeepSeekChatModel.builder()
                .stream(false)  // 是否流式输出
                .function_name_list(Arrays.asList("getWeather", "getCurrentDateTime")) // 调用本地工具
                .tool_choice("auto") // 工具选择
                .build();
    }
}

```

## RAG示例
1、开启RAG
```properties
deepseek.rag.enabled=true
```

2、配置向量仓库
```java
@Configuration
public class RagVectorStoreConfig {
    @Autowired
    private VectorStore vectorStore;
    @Autowired
    private DocumentLoader documentLoader;

    @PostConstruct
    public void init() throws IOException {
        List<Document> documents = documentLoader.loadDocumentsFromPath("/Users/xiaoyu/Documents/advice_doc");
        vectorStore.addDocuments("first",documents);
    }
}
```
3、使用
```java
@Log4j2
@Service
public class RagServiceImpl {
    // 直接注入
    @Autowired
    private RagUtils ragUtils;
    // 注入你上面定义的模型
    @Autowired
    private DeepSeekChatModel deepSeekChatModel;
    // 直接注入
    @Autowired
    private DeepSeekService deepSeekService;
    // RAG测试方法
    public void ragTest() {
        // 1. 执行RAG查询
        String query = "15岁可以当兵吗";
        log.info("执行查询: {}", query);
        // 构建RAG增强提示
        DSUserReqMessage ragPrompt = ragUtils.buildRagPrompt(query,"first");
        // 构建请求
        DeepSeekRequest request = new DeepSeekRequest();
        request.setDeepSeekModel(deepSeekChatModel);
        request.setMessages(Arrays.asList(ragPrompt));
        DSResponse dsResponse = deepSeekService.sendMessage(request);
        // 2. 输出结果
        log.info("\n输出结果:\n{}\n", dsResponse);
        log.info("=== RAG示例结束 ===");
    }
}
```

## 📝 版本演进
- **V3.1.0**: 增加余额查询服务
- **V3.0.0**: RAG
- **V2.5.3**: 修复DSUserReqMessage builder时无法设置content
- **V2.5.2**：工具内添加组件扫描，无需手动扫描
- **V2.5.0**：properties配置文件idea提示
- **V2.4.0**：新增工具调用功能，引入工具相关注解
- **V2.3.0**：适配SpringBoot 3.x框架
- **V2.2.0**：完善代码注释体系，添加Stream流解析
- **V2.0.0**：实现完整SDK封装，提供Spring Bean形式的模型
- **V1.0.0**：基础API封装，支持同步、异步请求

## 🤝 联系与交流

作者：小宇

![微信](img.png)

QQ群聊
![img_1.png](img_1.png)

## 📚 资源获取

- Gitee仓库：[https://gitee.com/xiaoyudi_xyz/deepseek](https://gitee.com/xiaoyudi_xyz/deepseek)
- Github仓库：[https://github.com/xiaoyu171222853/DeepSeek4J](https://github.com/xiaoyu171222853/DeepSeek4J)
- Maven中央仓库：[https://mvnrepository.com/artifact/top.aoyudi.deepseek/deepseek4j](https://mvnrepository.com/artifact/top.aoyudi.deepseek/deepseek4j)

---

希望DeepSeek4J能帮助Java开发者更便捷地集成AI能力，如有任何问题或建议，欢迎通过上述方式联系交流！