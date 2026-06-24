package com.ranyk.spring.ai.demo.api;

import cn.hutool.json.JSONObject;
import com.ranyk.spring.ai.demo.common.domain.vo.Result;
import com.ranyk.spring.ai.demo.domain.vo.TopicBook;
import com.ranyk.spring.ai.demo.domain.vo.TopicBookReview;
import com.ranyk.spring.ai.demo.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.List;

/**
 * CLASS_NAME: ChatApi.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 聊天 API 接口
 * @date: 2026-06-22
 */
@RestController
@RequestMapping("/chat/ai")
public class ChatApi {

    /**
     * 聊天服务对象
     */
    private final ChatService chatService;

    /**
     * 构造方法 - 向 Spring IOC 容器中注入有关业务逻辑处理类对象
     *
     * @param chatService 聊天服务对象
     */
    @Autowired
    public ChatApi(ChatService chatService) {
        this.chatService = chatService;
    }

    /**
     * 聊天接口 - 使用 OpenAI 接口调用 - 阿里云 - 百炼云平台 - qwen3.7-plus 大模型 - 阻塞式,等待所有结果一起返回
     *
     * @param question 用户输入的问题字符串
     * @return 返回聊天结果 {@link Result} 泛型对象, 封装了结果数据
     */
    @GetMapping("/dashscope")
    public Result<String> dashscope(String question) {
        return Result.success(chatService.dashscope(question));
    }

    /**
     * 聊天接口 - 使用 Ollama 引擎 调用 - 本地 - deepseek-r1:1.5b 大模型 - 阻塞式,等待所有结果一起返回
     *
     * @param question 用户输入的问题字符串
     * @return 返回聊天结果 {@link Result} 泛型对象, 封装了结果数据
     */
    @GetMapping("/ollama")
    public Result<String> ollama(String question) {
        return Result.success(chatService.ollama(question));
    }

    /**
     * 聊天接口 - 使用 OpenAI 接口调用 - 阿里云 - 百炼云平台 - qwen3.7-plus 大模型 - 流式返回
     *
     * @param question 用户输入的问题字符串
     * @return 返回聊天结果 {@link Flux} 流对象, 封装了结果数据
     */
    @GetMapping(value = "/dashscope/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ServerSentEvent<String>> dashscopeStream(String question) {
        Flux<ServerSentEvent<String>> contentStream = chatService.dashscopeStream(question).map(content -> buildSSEvent("data", "响应中...", content));
        return Flux.concat(
                Flux.just(buildSSEvent("start", "响应开始", "")),
                contentStream,
                Flux.just(buildSSEvent("end", "响应结束", ""))
        );
    }

    /**
     * 聊天接口 - 使用 Ollama 引擎 调用 - 本地 - deepseek-r1:1.5b 大模型 - 流式返回
     *
     * @param question 用户输入的问题字符串
     * @return 返回聊天结果 {@link Flux} 流对象, 封装了结果数据
     */
    @GetMapping(value = "/ollama/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ServerSentEvent<String>> ollamaStream(String question) {
        Flux<ServerSentEvent<String>> contentStream = chatService.ollamaStream(question).map(content -> buildSSEvent("data", "响应中...", content));
        return Flux.concat(
                Flux.just(buildSSEvent("start", "响应开始", "")),
                contentStream,
                Flux.just(buildSSEvent("end", "响应结束", ""))
        );
    }

    /**
     * 聊天接口 - 使用 OpenAI 接口调用 - 阿里云 - 百炼云平台 - qwen3.7-plus 大模型 - 使用 prompt 提示词模版 - 流式返回
     *
     * @param topic 用户输入的提示词信息
     * @return 返回聊天结果 {@link Result} 泛型对象, 封装了结果数据
     */
    @GetMapping(value = "/topic/dashscope/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ServerSentEvent<String>> topicDashscopeStream(String topic) {
        Flux<ServerSentEvent<String>> contentStream = chatService.topicDashscopeStream(topic)
                .map(content -> buildSSEvent("data", "响应中...", content));
        return Flux.concat(
                Flux.just(buildSSEvent("start", "响应开始", "")),
                contentStream,
                Flux.just(buildSSEvent("end", "响应结束", ""))
        );
    }

    /**
     * 聊天接口 - 使用 OpenAI 接口调用 - 阿里云 - 百炼云平台 - qwen3.7-plus 大模型 - 使用 user 提示词 ,同时设置 system 提示词 - 流式返回
     *
     * @param topic 用户输入的提示词信息
     * @return 返回聊天结果 {@link Flux} 流对象, 封装了结果数据
     */
    @GetMapping(value = "/topic/dashscope/system/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ServerSentEvent<String>> topicDashscopeSystemStream(String topic) {
        Flux<ServerSentEvent<String>> contentStream = chatService.topicDashscopeSystemStream(topic)
                .map(content -> buildSSEvent("data", "响应中...", content));
        return Flux.concat(
                Flux.just(buildSSEvent("start", "响应开始", "")),
                contentStream,
                Flux.just(buildSSEvent("end", "响应结束", ""))
        );
    }

    /**
     * 聊天接口 - 使用 OpenAI 接口调用 - 阿里云 - 百炼云平台 - qwen3.7-plus 大模型 - 使用系统设定 defaultSystem - 流式返回
     *
     * @param topic 用户输入的提示词信息
     * @return 返回聊天结果 {@link Flux} 流对象, 封装了结果数据
     */
    @GetMapping(value = "/topic/dashscope/system/stream/defaultSystem", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ServerSentEvent<String>> topicDashscopeDefaultSystemStream(String topic) {
        Flux<ServerSentEvent<String>> contentStream = chatService.topicDashscopeDefaultSystemStream(topic)
                .map(content -> buildSSEvent("data", "响应中...", content));
        return Flux.concat(
                Flux.just(buildSSEvent("start", "响应开始", "")),
                contentStream,
                Flux.just(buildSSEvent("end", "响应结束", ""))
        );
    }

    /**
     * 聊天接口 - 使用 OpenAI 接口调用 - 阿里云 - 百炼云平台 - qwen3.7-plus 大模型 - 使用 system 提示 - 用户 user 提示词 - 结构化输出 - 阻塞式,等待所有结果一起返回
     *
     * @param topic 用户输入的提示词信息
     * @return 返回聊天结果 {@link Result} 泛型对象, 封装了结果数据
     */
    @GetMapping(value = "/topic/dashscope/struct/output/entity/call")
    public Result<TopicBook> topicDashscopeStructOutputEntityCall(String topic) {
        return Result.success(chatService.topicDashscopeStructOutputEntityCall(topic));
    }

    /**
     * 聊天接口 - 使用 OpenAI 接口调用 - 阿里云 - 百炼云平台 - qwen3.7-plus 大模型 - 使用 system 提示 - 用户 user 提示词 - 结构化输出 - 阻塞式,等待所有结果一起返回 - 返回 List 集合
     *
     * @param bookName 图书名称
     * @return 结构化输出结果列表
     */
    @GetMapping(value = "/topic/dashscope/struct/output/entity/list/call")
    public Result<List<TopicBookReview>> topicDashscopeStructOutputEntityListCall(String bookName) {
        return Result.success(chatService.topicDashscopeStructOutputEntityListCall(bookName));
    }

    /**
     * 聊天接口 - 使用 OpenAI 接口调用 - 阿里云 - 百炼云平台 - qwen3.7-plus 大模型 - 使用用户 user 提示词 - 带有会话记忆 - 流式返回
     *
     * @param question       用户输入的问题字符串
     * @param conversationId 会话ID
     * @return 返回聊天结果 {@link Flux} 流对象, 封装了结果数据
     */
    @GetMapping(value = "/question/stream/chat/memory", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ServerSentEvent<String>> questionStreamChatMemory(String question, String conversationId) {
        Flux<ServerSentEvent<String>> contentStream = chatService.questionStreamChatMemory(question, conversationId)
                .map(content -> buildSSEvent("data", "响应中...", content));
        return Flux.concat(
                Flux.just(buildSSEvent("start", "响应开始", "")),
                contentStream,
                Flux.just(buildSSEvent("end", "响应结束", "")));
    }

    /**
     * 聊天接口 - 使用 OpenAI 接口调用 - 阿里云 - 百炼云平台 - qwen3.7-plus 大模型 - 添加日期时间工具 - 回答用户问题 - 阻塞式,等待所有结果一起返回
     *
     * @param question 用户输入的问题字符串
     * @return 返回聊天结果 {@link Result} 泛型对象, 封装了结果数据
     */
    @GetMapping("/dashscope/add/date/time/tool/answer/user/questions")
    public Result<String> dashscopeAddDateTimeToolAnsweringUserQuestions(String question) {
        return Result.success(chatService.dashscopeAddDateTimeToolAnsweringUserQuestions(question));
    }

    /**
     * 聊天接口 - 使用 OpenAI 接口调用 - 阿里云 - 百炼云平台 - text-embedding-v4 大模型 - 文本嵌入 - 转换文本为向量 - 阻塞式,等待所有结果一起返回
     *
     * @param text 用户输入的文本字符串
     * @return 返回聊天结果 {@link Result} 泛型对象, 封装了结果数据
     */
    @GetMapping("/dashscope/embedding/convert/text")
    public Result<String> dashscopeEmbeddingConvertText(String text) {
        return Result.success(chatService.dashscopeEmbeddingConvertText(text));
    }

    /**
     * 构建 SSE 事件对象
     *
     * @param eventType 事件类型
     * @param msg       提示信息
     * @param data      数据内容
     * @return SSE 事件对象
     */
    private ServerSentEvent<String> buildSSEvent(String eventType, String msg, String data) {
        // 创建 JSON 对象, 封装响应数据
        JSONObject json = new JSONObject();
        // 设置响应码
        json.set("code", "200");
        // 设置响应成功标志
        json.set("success", true);
        // 设置响应提示信息
        json.set("msg", msg);
        // 设置响应数据内容
        json.set("data", data);
        // 创建 SSE 事件对象
        return ServerSentEvent.<String>builder()
                // 设置数据内容
                .data(json.toString())
                // 设置事件类型
                .event(eventType)
                // 设置事件ID (可选)
                // .id("1234567890")
                // 设置事件时间 (可选)
                // .retry(Duration.ZERO)
                // 构建 SSE 事件对象
                .build();
    }
}
