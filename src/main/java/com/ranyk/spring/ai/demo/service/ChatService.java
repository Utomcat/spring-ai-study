package com.ranyk.spring.ai.demo.service;

import cn.hutool.json.JSONUtil;
import com.ranyk.spring.ai.demo.ai.tool.DataTimeTool;
import com.ranyk.spring.ai.demo.domain.vo.TopicBook;
import com.ranyk.spring.ai.demo.domain.vo.TopicBookReview;
import com.ranyk.spring.ai.demo.utils.MathUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.document.Document;
import org.springframework.ai.openai.OpenAiEmbeddingModel;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.redis.RedisVectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.*;

/**
 * CLASS_NAME: ChatService.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 聊天业务逻辑处理类
 * @date: 2026-06-22
 */
@Slf4j
@Service
public class ChatService {

    /**
     * 使用 OpenAI 接口调用 - 阿里云 - 百炼云平台 - qwen3.7-plus 大模型 - 不带会话记忆存储功能 - 聊天客户端对象 {@link ChatClient}
     */
    private final ChatClient dashscopeChatClient;
    /**
     * 使用 Ollama 接口调用 - 本地 - qwen3.6 大模型 - 聊天客户端对象 {@link ChatClient}
     */
    private final ChatClient ollamaChatClient;
    /**
     * 使用 OpenAI 接口调用 - 阿里云 - 百炼云平台 - qwen3.7-plus 大模型 - 专用于 Java 信息咨询的聊天客户端对象 {@link ChatClient}
     */
    private final ChatClient javaCounselorChatClient;
    /**
     * 使用 OpenAI 接口调用 - 阿里云 - 百炼云平台 - qwen3.7-plus 大模型 - 带有会话记忆存储功能 - 聊天客户端对象 {@link ChatClient}
     */
    private final ChatClient dashscopeInMemoryChatMemoryChatClient;
    /**
     * 使用 OpenAI 的 嵌入式 模型 - 嵌入模型 {@link OpenAiEmbeddingModel} - 使用 Spring AI 的自动配置创建的 嵌入模型 {@link OpenAiEmbeddingModel}
     */
    private final OpenAiEmbeddingModel openAiEmbeddingModel;
    /**
     * 向量存储 {@link VectorStore} 对象 - 使用 Spring AI 的自动配置创建的 向量存储 {@link VectorStore} 对象
     */
    private final VectorStore vectorStore;
    /**
     * Redis 向量存储 {@link RedisVectorStore} 对象 - 使用 Spring AI 的自动配置创建的 Redis 向量存储 {@link RedisVectorStore} 对象
     */
    private final RedisVectorStore redisVectorStore;
    /**
     * Spring AI 需要调用的日期时间工具类 {@link DataTimeTool}
     */
    private final DataTimeTool dataTimeTool;
    /**
     * 批量删除向量存储数据时, 每批次删除的数量, 如果未进行配置, 则默认为 10
     */
    @Value("${vector.data.delete.batch-quantity:10}")
    private Integer batchQuantity;

    /**
     * 构造方法 - 向 ChatService 对象中注入 ChatClient 对象
     *
     * @param dashscopeChatClient                   使用 OpenAI 接口 调用 - 阿里云 - 百炼云平台 - qwen3.7-plus 大模型 - 不带会话记忆存储功能 - 聊天客户端对象 {@link ChatClient}
     * @param ollamaChatClient                      使用 Ollama 引擎 调用 - 本地 - qwen3.6 大模型 - 聊天客户端对象 {@link ChatClient}
     * @param javaCounselorChatClient               使用 OpenAI 接口 调用 - 阿里云 - 百炼云平台 - qwen3.7-plus 大模型 - 专用于 Java 信息咨询的聊天客户端对象 {@link ChatClient}
     * @param dashscopeInMemoryChatMemoryChatClient 使用 OpenAI 接口 调用 - 阿里云 - 百炼云平台 - qwen3.7-plus 大模型 - 带有会话记忆存储功能 - 聊天客户端对象 {@link ChatClient}
     * @param openAiEmbeddingModel                  使用 OpenAI 的 嵌入式 模型 - 嵌入模型 {@link OpenAiEmbeddingModel}
     * @param vectorStore                           向量存储 {@link VectorStore} 对象- 使用 Spring AI 的自动配置创建的 嵌入模型 {@link OpenAiEmbeddingModel}
     * @param redisVectorStore                      Redis 向量存储 {@link RedisVectorStore} 对象- 使用 Spring AI 的自动配置创建的 Redis 向量存储 {@link RedisVectorStore} 对象
     * @param dataTimeTool                          Spring AI 需要调用的日期时间工具类 {@link DataTimeTool}
     */
    @Autowired
    public ChatService(ChatClient dashscopeChatClient,
                       ChatClient ollamaChatClient,
                       ChatClient javaCounselorChatClient,
                       ChatClient dashscopeInMemoryChatMemoryChatClient,
                       OpenAiEmbeddingModel openAiEmbeddingModel,
                       VectorStore vectorStore,
                       RedisVectorStore redisVectorStore,
                       DataTimeTool dataTimeTool) {
        this.dashscopeChatClient = dashscopeChatClient;
        this.ollamaChatClient = ollamaChatClient;
        this.javaCounselorChatClient = javaCounselorChatClient;
        this.dashscopeInMemoryChatMemoryChatClient = dashscopeInMemoryChatMemoryChatClient;
        this.openAiEmbeddingModel = openAiEmbeddingModel;
        this.vectorStore = vectorStore;
        this.redisVectorStore = redisVectorStore;
        this.dataTimeTool = dataTimeTool;
    }

    /**
     * 聊天处理方法 -  使用 OpenAI 接口调用 - 阿里云 - 百炼云平台 - qwen3.7-plus 大模型
     *
     * @param question 用户输入的提问
     * @return 聊天处理结果
     */
    public String dashscope(String question) {
        log.info("Current Use Dashscope Blockade Method, User Input Question => {}", question);
        return dashscopeChatClient
                // 创建一个提示词对象
                .prompt()
                // 添加用户输入的提问
                .user(question)
                // 调用大模型并返回结果
                .call()
                // 获取大模型的回复内容
                .content();
    }

    /**
     * 聊天处理方法 -  使用 Ollama 接口调用 - 本地 - deepseek-r1:1.5b 大模型
     *
     * @param question 用户输入的提问
     * @return 聊天处理结果
     */
    public String ollama(String question) {
        log.info("Current Use Ollama Blockade Method, User Input Question => {}", question);
        return ollamaChatClient
                // 创建一个提示词对象
                .prompt()
                // 添加用户输入的提问
                .user(question)
                // 调用大模型并返回结果
                .call()
                // 获取大模型的回复内容
                .content();
    }

    /**
     * 聊天处理方法 -  使用 OpenAI 接口调用 - 阿里云 - 百炼云平台 - qwen3.7-plus 大模型 - 流式返回
     *
     * @param question 用户输入的提问
     * @return 聊天处理结果
     */
    public Flux<String> dashscopeStream(String question) {
        log.info("Current Use Dashscope Stream Method, User Input Question => {}", question);
        return dashscopeChatClient
                // 创建一个提示词对象
                .prompt()
                // 添加用户输入的提问
                .user(question)
                // 调用大模型并返回结果
                .stream()
                // 获取大模型的回复内容
                .content();
    }

    /**
     * 聊天处理方法 -  使用 Ollama 接口调用 - 本地 - deepseek-r1:1.5b 大模型 - 流式返回
     *
     * @param question 用户输入的提问
     * @return 聊天处理结果
     */
    public Flux<String> ollamaStream(String question) {
        log.info("Current Use Ollama Stream Method, User Input Question => {}", question);
        return ollamaChatClient
                // 创建一个提示词对象
                .prompt()
                // 添加用户输入的提问
                .user(question)
                // 调用大模型并返回结果
                .stream()
                // 获取大模型的回复内容
                .content();
    }

    /**
     * 聊天处理方法 -  使用 OpenAI 接口调用 - 阿里云 - 百炼云平台 - qwen3.7-plus 大模型 - 使用提示词 - 流式返回
     *
     * @param topic 提示词
     * @return 响应结果信息 - 流式返回
     */
    public Flux<String> topicDashscopeStream(String topic) {
        log.info("Current Use Dashscope Topic Method, Set prompt and use PromptTemplate, User Input Topic => {}", topic);
        // 创建一个提示词模板对象
        PromptTemplate promptTemplate = new PromptTemplate("请简要的介绍一下 {topic}");
        // 设置提示词参数
        Prompt prompt = promptTemplate.create(Map.of("topic", topic));
        return dashscopeChatClient
                // 设置提示词
                .prompt(prompt)
                // 调用大模型并返回结果
                .stream()
                // 获取大模型的回复内容
                .content();
    }

    /**
     * 聊天处理方法 -  使用 OpenAI 接口调用 - 阿里云 - 百炼云平台 - qwen3.7-plus 大模型 - 使用提示词 - 流式返回 - 系统提示词
     *
     * @param topic 提示词
     * @return 响应结果信息 - 流式返回
     */
    public Flux<String> topicDashscopeSystemStream(String topic) {
        log.info("Current Use Dashscope Topic Method, Set system prompt and user prompt, User Input Topic => {}", topic);
        return dashscopeChatClient.prompt()
                // 设置系统提示词
                .system("你是一个专业的书评助手")
                // 设置用户提示词
                .user(u -> u.text("请向我推荐三本关于 {topic} 的书籍, 请只给出书籍名称").param("topic", topic))
                // 调用大模型并返回结果
                .stream()
                // 获取大模型的回复内容
                .content();
    }

    /**
     * 聊天处理方法 -  使用 OpenAI 接口调用 - 阿里云 - 百炼云平台 - qwen3.7-plus 大模型 - 专用于 Java 信息咨询的处理 - 流式返回
     *
     * @param topic 提示词
     * @return 响应结果信息 - 流式返回
     */
    public Flux<String> topicDashscopeDefaultSystemStream(String topic) {
        log.info("Current Use Dashscope Topic Method, Call Java Counselor Chat Client, Set user prompt, use stream return, User Input Topic => {}", topic);
        return javaCounselorChatClient.prompt()
                // 设置用户提示词
                .user(topic)
                // 调用大模型并返回结果
                .stream()
                // 获取大模型的回复内容
                .content();
    }

    /**
     * 聊天处理方法 -  使用 OpenAI 接口调用 - 阿里云 - 百炼云平台 - qwen3.7-plus 大模型 - 使用 system 提示 - 用户 user 提示词 - 结构化输出 - 阻塞式,等待所有结果一起返回
     *
     * @param topic 提示词
     * @return 响应结果信息 - 结构化输出 - 实体类返回
     */
    public TopicBook topicDashscopeStructOutputEntityCall(String topic) {
        log.info("Current Use Dashscope Blockade Method, User Input Topic => {}", topic);
        TopicBook topicBook = dashscopeChatClient.prompt()
                // 设置系统提示词
                .system("你是一个专业的书评助手")
                // 设置用户提示词
                .user(u -> u.text("请向我推荐三本关于 {topic} 的书籍, 只需要返回书名即可").param("topic", topic))
                // 调用大模型并返回结果
                .call()
                // 将大模型调用响应结果转换为 TopicBook 实体类对象返回
                .entity(TopicBook.class);
        log.info("Current Use Dashscope Blockade Method, User Input Topic => {}, TopicBook => {}", topic, topicBook);
        return topicBook;
    }

    /**
     * 聊天处理方法 -  使用 OpenAI 接口调用 - 阿里云 - 百炼云平台 - qwen3.7-plus 大模型 - 使用用户 user 提示词 - 结构化输出 - 阻塞式,等待所有结果一起返回
     *
     * @param bookName 书籍名称
     * @return 响应结果信息 - 结构化输出 - 实体类 List 集合返回
     */
    public List<TopicBookReview> topicDashscopeStructOutputEntityListCall(String bookName) {
        log.info("Current Use Dashscope Blockade Method, Return List<TopicBookReview>, User Input bookName => {}", bookName);
        List<TopicBookReview> topicBookReviewList = dashscopeChatClient.prompt()
                // 添加用户输入的提问
                .user(u -> u.text("请给{bookName}书籍三条评价信息").param("bookName", bookName))
                // 调用大模型并返回结果
                .call()
                // 将大模型调用响应结果转换为 TopicBookReview 实体类对象 List 返回
                .entity(new ParameterizedTypeReference<>() {
                });
        log.info("Current Use Dashscope Blockade Method, Return List<TopicBookReview>, User Input bookName => {}, TopicBookReviewList => {}", bookName, topicBookReviewList);
        return topicBookReviewList;
    }

    /**
     * 聊天处理方法 -  使用 OpenAI 接口调用 - 阿里云 - 百炼云平台 - qwen3.7-plus 大模型 - 使用用户 user 提示词 - 带会话记忆 - 流式返回结果
     *
     * @param question       用户输入的提问
     * @param conversationId 会话ID
     * @return 聊天处理结果
     */
    public Flux<String> questionStreamChatMemory(String question, String conversationId) {
        log.info("Current Use Dashscope Stream Method, and Storing session information, User Input Question => {}, ConversationId => {}", question, conversationId);
        return dashscopeInMemoryChatMemoryChatClient.prompt()
                // 添加用户输入的提问
                .user(question)
                // 设置会话ID
                .advisors(a -> a.param(ChatMemory.CONVERSATION_ID, conversationId))
                // 调用大模型并返回结果
                .stream()
                // 获取大模型的回复内容
                .content();

    }

    /**
     * 聊天处理方法 -  使用 OpenAI 接口调用 - 阿里云 - 百炼云平台 - qwen3.7-plus 大模型 - 并添加日期时间工具类(工具功能有: 获取当前时间) - 回答用户问题 - 阻塞式,等待所有结果一起返回
     *
     * @param question 用户输入的提问
     * @return 聊天处理结果
     */
    public String dashscopeAddDateTimeToolAnsweringUserQuestions(String question) {
        log.info("Current Use Dashscope Blockade Method, and Add DateTimeTool, User Input Question => {}", question);
        String content = dashscopeChatClient.prompt()
                // 添加用户输入的提问
                .user(question)
                // 添加 AI 需要调用的工具, 可传入多个工具
                .tools(dataTimeTool)
                // 调用大模型并返回结果
                .call()
                // 获取大模型的回复内容
                .content();
        log.info("Current Use Dashscope Blockade Method, and Add DateTimeTool, LLM Response => {}", content);
        return content;
    }

    /**
     * 聊天处理方法 -  使用 OpenAI 接口调用 - 阿里云 - 百炼云平台 - text-embedding-v4 大模型 - 嵌入式 模型 - 将文本转换为嵌入向量 - 阻塞式,等待所有结果一起返回
     *
     * @param text 需要转换的文本
     * @return 嵌入向量结果
     */
    public String dashscopeEmbeddingConvertText(String text) {
        log.info("Current Use Dashscope Embedding Method, Convert Text to Embedding => {}", text);
        // 调用 OpenAI 嵌入式模型, 将文本转换为嵌入向量, 并将转换结果返回为一个 float 数组
        float[] floats = openAiEmbeddingModel.embed(text);
        // 将 float 数组转换为 JSON 字符串
        String floatsString = JSONUtil.toJsonStr(floats);
        log.info("Current Use Dashscope Embedding Method, After converting the text to a vector float array, the array length is => {} ", floats.length);
        log.info("Current Use Dashscope Embedding Method, Converted text from the original text => {}", text);
        log.info("Current Use Dashscope Embedding Method, The result of the transformation => {} ", floatsString);
        // 返回调用 嵌入式 模型将文本转换后的嵌入向量结果
        return floatsString;
    }

    /**
     * 聊天处理方法 -  使用 OpenAI 接口调用 - 阿里云 - 百炼云平台 - text-embedding-v4 大模型 - 嵌入式 模型 - 将多段文本转换为嵌入向量, 并两两计算出两个向量之间的距离,通过使用的计算方式分别采用调用的 MathUtils 工具类的方法 - 阻塞式,等待所有结果一起返回
     *
     * @param texts 用户输入的多段需要转换的文本
     * @param type  距离计算方式类型, 1: 表示欧式距离计算方式; 2: 表示余弦距离计算方式; 3: 表示其他距离计算方式; 具体参见 {@link MathUtils.DistanceTypeEnum} 类
     * @return 距离计算结果
     */
    public String dashscopeEmbeddingConvertMultiTextAndGetEuclideanDistance(List<String> texts, Integer type) {
        log.info("Current Use Dashscope Embedding Method, Convert Multi Text to Embedding and Get Euclidean Distance, User Input Texts => {}", JSONUtil.toJsonStr(texts));
        if (Objects.isNull(texts) || texts.isEmpty() || texts.size() < 2) {
            log.error("Invalid input: texts is null, empty, or contains less than two elements");
            return "Invalid input: texts is null, empty, or contains less than two elements";
        }
        Map<String, float[]> textVectorMap = new HashMap<>(texts.size());
        texts.forEach(text -> textVectorMap.put(text, openAiEmbeddingModel.embed(text)));
        // 遍历 textVectorMap , 两两 调用 MathUtils 工具中的 calculateEuclideanDistance 方法计算两个向量之间的 欧式距离, 并将两两计算结果记录下来, 最后返回所有的计算结果, 不重复计算
        List<Map<String, Object>> distanceList = new ArrayList<>();
        List<String> keys = new ArrayList<>(textVectorMap.keySet());
        for (int i = 0; i < keys.size(); i++) {
            for (int j = i + 1; j < keys.size(); j++) {
                String text1 = keys.get(i);
                String text2 = keys.get(j);
                double distance = MathUtils.calculateDistance(textVectorMap.get(text1), textVectorMap.get(text2), MathUtils.DistanceTypeEnum.getByType(type));
                Map<String, Object> distanceInfo = new HashMap<>();
                distanceInfo.put("text1", text1);
                distanceInfo.put("text2", text2);
                distanceInfo.put("distance", distance);
                distanceInfo.put("type", MathUtils.DistanceTypeEnum.getNameByType(type));
                distanceList.add(distanceInfo);
            }
        }
        log.info("Current Use Dashscope Embedding Method, After calculating the Euclidean distance, the result is => {}", JSONUtil.toJsonStr(distanceList));
        return JSONUtil.toJsonStr(distanceList);
    }

    /**
     * 使用 OpenAI 向量存储服务, 存储用户输入的文本向量数据
     *
     * @param texts 用户输入的文本内容
     * @return 存储结果
     */
    public String vectorStoreUserInputText(List<String> texts) {
        log.info("Current Use OpenAI Vector Store Method, User Input Texts => {}", JSONUtil.toJsonStr(texts));
        // 校验参数 - 判定用户输入的文本是否为空
        if (Objects.isNull(texts) || texts.isEmpty()) {
            log.error("Invalid input: texts is null or empty");
            return "Invalid input: texts is null or empty";
        }
        // 将用户输入的文本内容转换为 Document 对象 List 集合
        List<Document> docs = List.of(texts.stream().map(Document::new).toList().toArray(new Document[0]));
        // 调用向量存储对象, 向 Redis 中存储向量数据
        vectorStore.add(docs);
        log.info("Current Use OpenAI Vector Store Method, After storing the vector data in Redis, Current Store Size => {}", docs.size());
        return "Success";
    }

    /**
     * 使用 OpenAI 向量存储服务, 查询与用户输入的文本最相似的文本数据
     *
     * @param text 用户输入的文本内容
     * @return 查询结果
     */
    public String vectorStoreSimilarityQueryUserInputText(String text) {
        log.info("Current Use OpenAI Vector Store Method, Query Similarity User Input Text, Current User Input Text => {}", text);
        // 校验参数 - 判定用户输入的文本是否为空
        if (Objects.isNull(text) || text.isEmpty()) {
            log.error("Invalid input: text is null or empty");
            return "Invalid input: text is null or empty";
        }
        // 调用向量存储对象, 进行相似查询
        List<Document> documents = vectorStore.similaritySearch(
                // 构建相似查询的查询请求对象
                SearchRequest.builder()
                        // 设置查询相似的 文本内容
                        .query(text)
                        // 设置查询相似的文本数量
                        .topK(2)
                        .build());
        log.info("Current Use OpenAI Vector Store Method, Query Similarity User Input Text, Similarity Search Size => {} , Result => {}", documents.size(), JSONUtil.toJsonStr(documents));
        return JSONUtil.toJsonStr(documents);
    }

    /**
     * 使用 OpenAI 向量存储服务, 查询所有文档的 ID , 一般不建议使用这种方式, 因为这种操作, 一旦文档数量过多, 将会非常耗时, 从而导致性能下降
     *
     * @return 返回所有文档的 ID
     */
    public List<String> vectorStoreAllDocumentsOfId() {
        log.info("Current Use OpenAI Vector Store Method, Query All Documents Of Id");
        // 获取总文档数
        long count = redisVectorStore.count();
        // 查询所有的文档数据
        List<Document> documentList = redisVectorStore.similaritySearch(SearchRequest.builder()
                // 设置查询所有文档
                .query("")
                // 设置查询所有文档数量
                .topK((int) count)
                // 构建查询请求对象
                .build());
        log.info("Current Use OpenAI Vector Store Method, Query All Documents Of Id, Total Size => {}", documentList.size());
        // 遍历文档数据, 获取文档的 ID , 并返回
        return documentList.stream().map(Document::getId).toList();
    }

    /**
     * 使用 OpenAI 向量存储服务, 删除指定 ID 的文档数据
     *
     * @param id 文档 ID
     * @return 删除结果
     */
    public String vectorStoreDeleteDocumentById(String id) {
        log.info("Current Use OpenAI Vector Store Method, Delete Document By Id, Document Id => {}", id);
        // 校验参数 - 判定文档 ID 是否为空
        if (Objects.isNull(id) || id.isEmpty()) {
            log.error("Invalid input: id is null or empty");
            return "Invalid input: id is null or empty";
        }
        // 调用向量存储对象, 删除文档, 使用 文档 ID 列表
        redisVectorStore.delete(List.of(id));
        log.info("Current Use OpenAI Vector Store Method, Delete Document By Id, Document Id => {} , Result => {}", id, "Success");
        return "Success";
    }

    /**
     * 使用 OpenAI 向量存储服务, 删除所有文档数据
     *
     * @return 删除结果
     */
    public String vectorStoreDeleteAllDocuments() {
        log.info("Current Use OpenAI Vector Store Method, Delete All Documents");
        // 查询当前存在的文档数量
        long count = redisVectorStore.count();
        if (count <= 0) {
            log.info("Current Use OpenAI Vector Store Method, Delete All Documents, The data volume is 0,Return directly");
            return "No Operation";
        }
        // 求 count / batchQuantity 向上取整
        int batchCount = (int) Math.ceil(count / (double) batchQuantity);
        for (int i = 0; i < batchCount; i++) {
            List<String> ids = redisVectorStore.similaritySearch(SearchRequest.builder()
                    .query("")
                    .topK(batchQuantity)
                    .build()).stream().map(Document::getId).toList();
            redisVectorStore.delete(ids);
        }
        log.info("Current Use OpenAI Vector Store Method, Delete All Documents size => {}, Result => {}", count, "Success");
        return "Success";
    }
}
