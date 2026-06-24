package com.ranyk.spring.ai.demo.config;

import com.ranyk.spring.ai.demo.advisor.MySimpleLoggerAdvisor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.InMemoryChatMemoryRepository;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * CLASS_NAME: OpenAiConfiguration.java
 *
 * @author ranyk
 * @version V1.0
 * @description: openai 的相关配置
 * @date: 2026-06-22
 */
@Slf4j
@Configuration
public class OpenAiConfiguration {

    /**
     * 创建 ChatClient 对象 - 使用 OpenAI 接口调用 - 阿里云 - 百炼云平台 - qwen3.7-plus 大模型 - 不带会话记忆存储功能
     *
     * @param openAiChatModel       传入的 openAiChatModel 对象 {@link OpenAiChatModel}
     * @param mySimpleLoggerAdvisor 传入的 mySimpleLoggerAdvisor 对象 {@link MySimpleLoggerAdvisor}
     * @return 返回创建好的 ChatClient 对象 {@link ChatClient}
     */
    @Bean
    public ChatClient dashscopeChatClient(OpenAiChatModel openAiChatModel, MySimpleLoggerAdvisor mySimpleLoggerAdvisor) {
        return ChatClient
                // 创建 ChatClient 对象, 以及设置模型为 model
                .builder(openAiChatModel)
                // 添加 AOP 切面 Advisors, 用于增强 ChatClient 对象的功能
                .defaultAdvisors(
                        // 添加自定义的 AOP 切面拦截器
                        mySimpleLoggerAdvisor)
                // 构建 ChatClient 对象
                .build();
    }

    /**
     * 创建 ChatClient 对象 - 使用 OpenAI 接口调用 - 阿里云 - 百炼云平台 - qwen3.7-plus 大模型 - 带会话记忆存储功能
     *
     * @param openAiChatModel       传入的 openAiChatModel 对象 {@link OpenAiChatModel}
     * @param mySimpleLoggerAdvisor 传入的 mySimpleLoggerAdvisor 对象 {@link MySimpleLoggerAdvisor}
     * @param inMemoryChatMemory    传入的 inMemoryChatMemory 对象 {@link ChatMemory} , 用于会话记忆存储
     * @return 返回创建好的 ChatClient 对象 {@link ChatClient}
     */
    @Bean
    public ChatClient dashscopeInMemoryChatMemoryChatClient(OpenAiChatModel openAiChatModel, MySimpleLoggerAdvisor mySimpleLoggerAdvisor, ChatMemory inMemoryChatMemory) {
        return ChatClient
                // 创建 ChatClient 对象, 以及设置模型为 model
                .builder(openAiChatModel)
                // 添加 AOP 切面 Advisors, 用于增强 ChatClient 对象的功能
                .defaultAdvisors(
                        // 添加自定义的 AOP 切面拦截器
                        mySimpleLoggerAdvisor,
                        // 添加会话记忆 AOP 切面拦截器, 当前使用 内存会话记忆存储
                        MessageChatMemoryAdvisor.builder(inMemoryChatMemory).build())
                // 构建 ChatClient 对象
                .build();
    }

    /**
     * 创建 ChatClient 对象 - 使用 Ollama 接口调用 - 本地 - deepseek-r1:1.5b 大模型
     *
     * @param ollamaChatModel       传入的 ollamaChatModel 对象 {@link OllamaChatModel}
     * @param mySimpleLoggerAdvisor 传入的 mySimpleLoggerAdvisor 对象 {@link MySimpleLoggerAdvisor}
     * @return 返回创建好的 ChatClient 对象 {@link ChatClient}
     */
    @Bean
    public ChatClient ollamaChatClient(OllamaChatModel ollamaChatModel, MySimpleLoggerAdvisor mySimpleLoggerAdvisor) {
        return ChatClient
                // 创建 ChatClient 对象, 以及设置模型为 model
                .builder(ollamaChatModel)
                // 添加 AOP 切面 Advisors, 用于增强 ChatClient 对象的功能
                .defaultAdvisors(mySimpleLoggerAdvisor)
                // 构建 ChatClient 对象
                .build();
    }

    /**
     * 创建 ChatClient 对象 - 使用 OpenAI 接口调用 - 阿里云 - 百炼云平台 - qwen3.7-plus 大模型 - 指定当前的模型专用于 Java 技术问题回答
     *
     * @param openAiChatModel       传入的 openAiChatModel 对象 {@link OpenAiChatModel}
     * @param mySimpleLoggerAdvisor 传入的 mySimpleLoggerAdvisor 对象 {@link MySimpleLoggerAdvisor}
     * @return 返回创建好的 ChatClient 对象 {@link ChatClient}
     */
    @Bean
    public ChatClient javaCounselorChatClient(OpenAiChatModel openAiChatModel, MySimpleLoggerAdvisor mySimpleLoggerAdvisor) {
        String systemPrompt = """
                你是一个资深的 Java 技术顾问.
                禁止回答任何非技术类的问题, 比如: 天气、气候、娱乐信息、八卦信息等.
                如果用户提的问题是非 Java 问题, 请直接回复: 你当前只会 Java 技术, 无法回答非 Java 问题, 请使用其他 AI 工具或助手.
                代码示例必须符合  Java 21+ 规范.
                回答需要符合一下格式:
                    1. 一句话概括问题的核心
                    2. 提供详细的代码示例
                    3. 补充注意事项
                如果自己不确定可, 可以回复: 关于这个问题, 我目前没有明确的信息支持我做出对应的回答, 请提供相关的信息供我分析后, 在做回答.
                严禁胡编乱造回答
                """;

        return ChatClient
                // 创建 ChatClient 对象, 以及设置模型为 model
                .builder(openAiChatModel)
                // 添加一个默认的系统提示, 或称为指定当前系统的行为
                .defaultSystem(systemPrompt)
                // 添加 AOP 切面 Advisors, 用于增强 ChatClient 对象的功能
                .defaultAdvisors(mySimpleLoggerAdvisor)
                // 构建 ChatClient 对象
                .build();
    }

    /**
     * 创建 ChatMemory 对象, 用于在 ChatClient 和 ChatModel 之间进行会话记忆
     *
     * @return 返回创建好的 ChatMemory 对象 {@link ChatMemory} 对象
     */
    @Bean
    public ChatMemory inMemoryChatMemory() {
        return MessageWindowChatMemory.builder()
                // 设置最大消息数, 超过指定的消息数, 会自动删除最旧的消息
                .maxMessages(10)
                // 设置聊天内存仓库 - 当前配置为内存存储; 可选项有:
                // InMemoryChatMemoryRepository: 内存存储, 系统默认, 无需额外引入
                // RedisChatMemoryRepository: redis 存储, 需要额外引入依赖: spring-ai-starter-chat-memory-redis  和 spring-boot-starter-data-redis
                // JdbcChatMemoryRepository: jdbc 存储, 需要额外引入依赖: spring-ai-starter-model-chat-memory-repository-jdbc  和 对应的关系型数据库 Java 驱动
                // CassandraChatMemoryRepository: Cassandra 存储, 需要额外引入依赖: spring-ai-starter-model-chat-memory-repository-cassandra  和 Cassandra 的驱动
                // etc.
                .chatMemoryRepository(new InMemoryChatMemoryRepository())
                .build();
    }

    /**
     * 创建 MySimpleLoggerAdvisor 对象
     *
     * @return 返回创建好的 MySimpleLoggerAdvisor 对象 {@link MySimpleLoggerAdvisor}
     */
    @Bean
    @ConditionalOnMissingBean
    public MySimpleLoggerAdvisor mySimpleLoggerAdvisor() {
        log.info("Create Customization Advisor Object: MySimpleLoggerAdvisor Object , Used to enhance ChatClient functionality");
        return new MySimpleLoggerAdvisor();
    }
}
