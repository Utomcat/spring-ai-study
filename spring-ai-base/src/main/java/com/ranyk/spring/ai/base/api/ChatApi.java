package com.ranyk.spring.ai.base.api;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import com.ranyk.spring.ai.base.common.domain.vo.Result;
import com.ranyk.spring.ai.base.domain.mapstruct.file.query.QuerySimilarityFileMapper;
import com.ranyk.spring.ai.base.domain.mapstruct.file.upload.UploaderMapper;
import com.ranyk.spring.ai.base.domain.request.QuerySimilarityFileRequest;
import com.ranyk.spring.ai.base.domain.request.UploaderRequest;
import com.ranyk.spring.ai.base.domain.vo.TopicBook;
import com.ranyk.spring.ai.base.domain.vo.TopicBookReview;
import com.ranyk.spring.ai.base.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Flux;

import java.util.Arrays;
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
     * 文件上传基础数据转换映射接口对象
     */
    private final UploaderMapper uploaderMapper;
    /**
     * 文件相似度查询基础数据转换映射接口对象
     */
    private final QuerySimilarityFileMapper querySimilarityFileMapper;

    /**
     * 构造方法 - 向 Spring IOC 容器中注入有关业务逻辑处理类对象
     *
     * @param chatService               聊天服务对象
     * @param uploaderMapper            文件上传基础数据转换映射接口对象
     * @param querySimilarityFileMapper 文件相似度查询基础数据转换映射接口对象
     */
    @Autowired
    public ChatApi(ChatService chatService, UploaderMapper uploaderMapper, QuerySimilarityFileMapper querySimilarityFileMapper) {
        this.chatService = chatService;
        this.uploaderMapper = uploaderMapper;
        this.querySimilarityFileMapper = querySimilarityFileMapper;
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
     * 聊天接口 - 使用 OpenAI 接口调用 - 阿里云 - 百炼云平台 - text-embedding-v4 大模型 - 多文本嵌入 - 转换文本为向量, 并两两计算相对应的距离, 通过传入的 type 参数指定计算方式 - 阻塞式,等待所有结果一起返回
     *
     * @param texts 用户输入的多个文本字符串 List 集合
     * @param type  距离计算方式类型, 1: 表示欧式距离计算方式; 2: 表示余弦距离计算方式; 3: 表示其他距离计算方式; 具体参见 {@link com.ranyk.spring.ai.demo.utils.MathUtils.DistanceTypeEnum} 类
     * @return 返回聊天结果 {@link Result} 泛型对象, 封装了结果数据
     */
    @GetMapping("/dashscope/embedding/convert/multi/text")
    public Result<String> dashscopeEmbeddingConvertMultiText(@RequestParam List<String> texts, @RequestParam Integer type) {
        return Result.success(chatService.dashscopeEmbeddingConvertMultiTextAndGetEuclideanDistance(texts, type));
    }

    /**
     * 调用 ChatService 的向量存储方法, 将用户传入的 List&lt;String&gt; 文本信息存储到 Redis 向量存储中
     *
     * @param texts 用户输入的文本信息 List 集合
     * @return 调用结果 {@link Result} 泛型对象, 封装了结果数据
     */
    @GetMapping("/vector/store/user/input/text")
    public Result<String> vectorStoreUserInputText(@RequestParam List<String> texts) {
        return Result.success(chatService.vectorStoreUserInputText(texts));
    }

    /**
     * 调用 ChatService 的相似性查询方法, 依据用户传入的 文本信息 text 进行相似行文本查询
     *
     * @param text 用户输入的文本信息
     * @return 调用结果 {@link Result} 泛型对象, 封装了结果数据
     */
    @GetMapping("/vector/similarity/query/user/input/text")
    public Result<String> vectorSimilarityQueryUserInputText(@RequestParam String text) {
        return Result.success(chatService.vectorStoreSimilarityQueryUserInputText(text));
    }

    /**
     * 调用 ChatService 的向量ID 获取方法, 查询当前 Redis 向量数据库中所有的文档数据 ID
     *
     * @return 调用结果 {@link Result} 泛型对象, 封装了结果数据
     */
    @GetMapping("/vector/store/all/documents/id")
    public Result<List<String>> vectorStoreAllDocumentsOfId() {
        return Result.success(chatService.vectorStoreAllDocumentsOfId());
    }

    /**
     * 调用 ChatService 的向量删除方法, 依据用户传入的文档数据 ID 删除对应的向量数据
     *
     * @param id 用户输入的文档数据 ID
     * @return 调用结果 {@link Result} 泛型对象, 封装了结果数据
     */
    @GetMapping("/vector/store/delete/document/id")
    public Result<String> vectorStoreDeleteDocumentById(@RequestParam String id) {
        return Result.success(chatService.vectorStoreDeleteDocumentById(id));
    }

    /**
     * 调用 ChatService 的向量删除所有方法, 删除 Redis 向量数据库中所有的文档数据 - 谨慎操作
     *
     * @return 调用结果 {@link Result} 泛型对象, 封装了结果数据
     */
    @GetMapping("/vector/store/delete/all/documents")
    public Result<String> vectorStoreDeleteAllDocuments() {
        return Result.success(chatService.vectorStoreDeleteAllDocuments());
    }

    /**
     * 调用 ChatService 的文件上传和向量存储方法, 依据用户传入的文件信息进行文件上传和向量存储
     *
     * @param files           用户输入的文件信息
     * @param uploaderRequest 文件上传请求参数
     * @return 调用结果 {@link Result} 泛型对象, 封装了结果数据
     */
    @PostMapping(value = "/upload/file/vector/store", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Result<String> uploadFileAndVectorStore(@RequestParam("files") MultipartFile[] files, @ModelAttribute UploaderRequest uploaderRequest) {
        // 过滤掉空文件: 文件名为空或文件大小为0的无效文件
        List<MultipartFile> validFiles = Arrays.stream(files)
                .filter(file -> file != null && StrUtil.isNotBlank(file.getOriginalFilename()) && file.getSize() > 0)
                .toList();
        return Result.success(chatService.uploadFileAndVectorStore(validFiles, uploaderMapper.uploaderRequestToUploaderDTO(uploaderRequest)));
    }

    /**
     * 调用 ChatService 的文件相似度查询方法, 依据用户传入的文件信息进行文件相似度查询
     *
     * @param querySimilarityFileRequest 文件相似度查询请求参数 {@link QuerySimilarityFileRequest}
     * @return 文件相似度查询结果 {@link Result} 泛型对象, 封装了结果数据
     */
    @GetMapping("/similarity/search/vector/store/file")
    public Result<String> similaritySearchToVectorStoreFile(@ModelAttribute QuerySimilarityFileRequest querySimilarityFileRequest) {
        return Result.success(chatService.similaritySearchToVectorStoreFile(querySimilarityFileMapper.querySimilarityFileRequestToQuerySimilarityFileDTO(querySimilarityFileRequest)));
    }

    /**
     * 调用 ChatService 的 QuestionAnswerAdvisor  RAG 检索增强对文件相似度查询方法, 依据用户传入的文件信息进行文件相似度查询, 并返回相似文件内容, 以及文件内容中与问题最相似的句子, 并返回句子对应的答案
     *
     * @param querySimilarityFileRequest 文件相似度查询请求参数 {@link QuerySimilarityFileRequest}
     * @return 文件相似度查询结果 {@link Result} 泛型对象, 封装了结果数据
     */
    @GetMapping("/similarity/search/vector/store/file/with/question/answer/advisor")
    public Result<String> similaritySearchToVectorStoreFileWithQuestionAnswerAdvisor(@ModelAttribute QuerySimilarityFileRequest querySimilarityFileRequest) {
        return Result.success(chatService.similaritySearchToVectorStoreFileWithQuestionAnswerAdvisor(querySimilarityFileMapper.querySimilarityFileRequestToQuerySimilarityFileDTO(querySimilarityFileRequest)));
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
