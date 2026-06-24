# spring-ai-study - Spring AI 大模型集成示例项目

## 📖 项目简介

spring-ai-study 是一个基于 **Spring Boot 4.1.1** 和 **Spring AI 2.0.0** 构建的大语言模型集成示例项目。该项目演示了如何使用
Spring AI 框架集成多种大模型服务（阿里云百炼平台 qwen3.7-plus 和本地 Ollama deepseek-r1:1.5b），并展示了流式响应、会话记忆、结构化输出、工具调用等高级
AI 功能。

---

## 🚀 技术栈

| 技术          | 版本             | 说明          |
|-------------|----------------|-------------|
| Java        | 21             | 使用虚拟线程等现代特性 |
| Spring Boot | 4.1.1-SNAPSHOT | 应用框架        |
| Spring AI   | 2.0.0          | AI 集成框架     |
| Lombok      | 1.18.46        | 简化代码        |
| MapStruct   | 1.7.0.Beta1    | 对象映射        |
| Hutool      | 5.8.46         | Java 工具库    |
| JLayer      | 1.0.1          | MP3 音频播放    |

---

## 📁 项目结构

```
spring-ai-study/
├── src/main/java/com/ranyk/spring/ai/demo/
│   ├── SpringAiDemoOneApplication.java      # 应用启动类
│   ├── api/
│   │   └── ChatApi.java                     # 聊天 REST API 接口
│   ├── service/
│   │   ├── ChatService.java                 # 聊天业务逻辑处理
│   │   └── DelayedTaskService.java          # 延迟任务服务
│   ├── config/
│   │   ├── OpenAiConfiguration.java         # AI 模型和 ChatClient 配置
│   │   ├── VirtualThreadConfig.java         # 虚拟线程配置
│   │   └── FaviconConfig.java              # 网站图标配置
│   ├── advisor/
│   │   └── MySimpleLoggerAdvisor.java       # 自定义请求/响应日志拦截器
│   ├── ai/tool/
│   │   └── DataTimeTool.java                # AI 可调用的时间工具类
│   ├── domain/vo/
│   │   ├── TopicBook.java                   # 书籍推荐结构化输出 VO
│   │   └── TopicBookReview.java            # 书评结构化输出 VO
│   ├── common/domain/vo/
│   │   └── Result.java                      # 统一响应结果封装
│   └── handle/
│       └── GlobalWebExceptionHandler.java   # 全局异常处理器
└── src/main/resources/
    └── application.yml                      # 应用配置文件
```

---

## ✨ 核心功能

### 1. 多模型支持

- **阿里云百炼平台 (DashScope)**: 通过 OpenAI 兼容接口调用 qwen3.7-plus 模型
- **本地 Ollama**: 调用本地部署的 deepseek-r1:1.5b 模型

### 2. 聊天模式

#### 阻塞式调用

等待模型完整响应后一次性返回结果。

#### 流式调用 (Server-Sent Events)

实时返回模型生成的内容片段，提供更好的用户体验。

### 3. 提示词工程

- **基础提示词**: 直接传入用户问题
- **提示词模板**: 使用 `PromptTemplate` 进行参数化提示
- **System + User 提示词**: 设置系统角色和用户输入
- **默认系统提示词**: 为特定场景预设系统行为（如 Java 技术顾问）

### 4. 结构化输出

将 AI 响应自动映射到 Java 实体类：

- `TopicBook`: 主题和书籍列表
- `TopicBookReview`: 用户、评分、评论内容
- 支持单个对象和 List 集合输出

### 5. 会话记忆 (Chat Memory)

- 使用 `MessageWindowChatMemory` 实现对话历史管理
- 支持通过 `conversationId` 区分不同会话
- 可配置最大消息数（当前为 10 条）
- 支持多种存储后端：内存、Redis、JDBC、Cassandra 等

### 6. 工具调用 (Function Calling)

AI 可以调用预定义的 Java 方法：

- **获取当前时间**: 根据用户时区返回实时时间（格式：yyyy-MM-dd HH:mm:ss）
- **设置闹钟**: 在指定时间触发提醒（支持在线音频播放）
- **延迟任务调度**: 基于虚拟线程的定时任务执行

### 7. AOP 增强 (Advisor)

自定义 `MySimpleLoggerAdvisor` 拦截器（实现 `CallAdvisor` 和 `StreamAdvisor` 接口）：

- **阻塞式请求日志**: 记录请求前后完整信息及响应内容长度
- **流式请求日志**: 实时记录每个响应片段（Fragment）
- **统一命名**: `mySimpleLoggerAdvisor`，优先级 0（最高）
- **便于调试和性能分析**: 自动记录所有 ChatClient 调用

### 8. 虚拟线程

使用 Java 21 虚拟线程处理异步任务（基于 `Executors.newVirtualThreadPerTaskExecutor()`）：

- **延迟任务执行**: 支持延迟指定时间后执行任务（无返回值/带返回值）
- **定时任务调度**: 在指定时间点执行任务（基于毫秒时间戳）
- **周期性任务**: 支持固定延迟的周期性执行
- **高并发优化**: 轻量级线程，适合大量 I/O 密集型任务
- **资源管理**: 自动资源释放（@PreDestroy 优雅关闭，等待 5 秒后强制关闭）

### 9. 文本嵌入 (Embedding)

使用 `text-embedding-v4` 模型进行文本向量化：

- **单文本嵌入**: 将文本转换为 2048 维向量（float 数组）
- **多文本嵌入**: 批量转换文本并计算向量间距离
- **距离计算**:
    - 欧式距离（type=1）：`Math.sqrt(Σ(aᵢ-bᵢ)²)`
    - 余弦距离（type=2）：`1 - cosine_similarity`，范围 [0, 2]，值越小越相似
    - 其他距离（type=其他）：预留扩展
- **维度校验**: 自动检测向量维度一致性，不一致时返回 -1.0
- **应用场景**: 语义相似度计算、文本分类、检索增强生成 (RAG)

### 10. 向量存储与检索 (Vector Store)

使用 Redis 向量数据库（Redis Stack + RedisSearch）存储和检索向量：

- **向量存储**: 将文本转换为向量后存储到 Redis（索引名：`ranyk_index`，键前缀：`ranyk_vector_`）
- **相似度搜索**: 基于语义相似度查询最相似的文档（支持设置 topK）
- **文档管理**:
    - 查询所有文档 ID（通过 `RedisVectorStore.count()` 获取总数）
    - 按 ID 删除指定文档
    - 批量删除所有文档（按批次删除，每批次数量可配置，默认 100）
- **配置项**:
    - `spring.ai.vectorstore.redis.initialize-schema`: 是否初始化向量索引
    - `spring.ai.vectorstore.redis.index-name`: RedisSearch 索引名称
    - `spring.ai.vectorstore.redis.prefix`: 向量文档键前缀
    - `vector.data.delete.batch-quantity`: 批量删除时每批次的数量
- **应用场景**: 语义搜索、RAG（检索增强生成）、知识库构建、文档推荐

---

## 🔧 API 接口文档

所有接口基础路径: `/chat/ai`

### DashScope 模型接口

| 接口路径                                                  | 方法  | 说明                 | 参数                           |
|-------------------------------------------------------|-----|--------------------|------------------------------|
| `/dashscope`                                          | GET | 阻塞式聊天              | `question`                   |
| `/dashscope/stream`                                   | GET | 流式聊天 (SSE)         | `question`                   |
| `/topic/dashscope/stream`                             | GET | 提示词模板流式            | `topic`                      |
| `/topic/dashscope/system/stream`                      | GET | System+User 提示词流式  | `topic`                      |
| `/topic/dashscope/system/stream/defaultSystem`        | GET | 默认系统提示词流式（Java 顾问） | `topic`                      |
| `/topic/dashscope/struct/output/entity/call`          | GET | 结构化输出-书籍推荐         | `topic`                      |
| `/topic/dashscope/struct/output/entity/list/call`     | GET | 结构化输出-书评列表         | `bookName`                   |
| `/question/stream/chat/memory`                        | GET | 带会话记忆的流式聊天         | `question`, `conversationId` |
| `/dashscope/add/date/time/tool/answer/user/questions` | GET | 工具调用-回答问题（时间/闹钟）   | `question`                   |
| `/dashscope/embedding/convert/text`                   | GET | 文本嵌入-单文本向量化        | `text`                       |
| `/dashscope/embedding/convert/multi/text`             | GET | 文本嵌入-多文本向量化+距离计算   | `texts`, `type`              |
| `/vector/store/user/input/text`                       | GET | 向量存储-存储文本到Redis向量库 | `texts` (List)               |
| `/vector/similarity/query/user/input/text`            | GET | 向量查询-相似度搜索         | `text`                       |
| `/vector/store/all/documents/id`                      | GET | 向量管理-获取所有文档ID      | 无                            |
| `/vector/store/delete/document/id`                    | GET | 向量管理-删除指定文档        | `id`                         |
| `/vector/store/delete/all/documents`                  | GET | 向量管理-删除所有文档        | 无                            |

### Ollama 模型接口

| 接口路径             | 方法  | 说明         | 参数         |
|------------------|-----|------------|------------|
| `/ollama`        | GET | 阻塞式聊天      | `question` |
| `/ollama/stream` | GET | 流式聊天 (SSE) | `question` |

---

## ⚙️ 配置说明

### 环境变量

在 `application.yml` 中使用的环境变量：

```yaml
DASHSCOPE_OPEN_AI_API_KEY    # 阿里云百炼平台 API 密钥
OPENAI_LLM_MODEL             # DashScope 模型名称 (默认: qwen3.7-plus)
OLLAMA_LLM_MODEL             # Ollama 模型名称 (默认: deepseek-r1:1.5b)
EMBEDDING_LLM_MODEL          # 嵌入模型名称 (默认: text-embedding-v4)
```

### 关键配置项

```yaml
spring:
  ai:
    openai:
      api-key: ${DASHSCOPE_OPEN_AI_API_KEY}
      base-url: https://dashscope.aliyuncs.com/compatible-mode/v1
      chat:
        model: ${OPENAI_LLM_MODEL:qwen3.7-plus}
        timeout: 120s
      embedding:
        model: ${EMBEDDING_LLM_MODEL:text-embedding-v4}
        dimensions: 2048
    ollama:
      base-url: http://localhost:11434
      chat:
        model: ${OLLAMA_LLM_MODEL:deepseek-r1:1.5b}
    model:
      embedding: openai
    vectorstore:
      redis:
        initialize-schema: true
        index-name: ranyk_index
        prefix: ranyk_vector_
  data:
    redis:
      client-type: jedis
      host: 192.168.31.83
      port: 16380
      database: 0
      jedis:
        pool:
          enabled: false

# 自定义向量配置
vector:
  data:
    delete:
      batch-quantity: 100
```

---

## 🏃 快速开始

### 前置要求

1. **Java 21+** 已安装
2. **Maven 3.8+** 已安装
3. 阿里云百炼平台 API 密钥（如需使用 DashScope）
4. 本地安装 Ollama（如需使用本地模型）

### 安装步骤

1. **克隆项目**
   ```bash
   git clone <repository-url>
   cd spring-ai-study
   ```

2. **配置环境变量**

   Windows (PowerShell):
   ```powershell
   $env:DASHSCOPE_OPEN_AI_API_KEY="your-api-key-here"
   ```

   Linux/Mac:
   ```bash
   export DASHSCOPE_OPEN_AI_API_KEY="your-api-key-here"
   ```

3. **安装 Ollama（可选）**
   ```bash
   # 访问 https://ollama.ai 下载安装
   ollama pull deepseek-r1:1.5b
   ```

4. **编译项目**
   ```bash
   mvn clean install
   ```

5. **运行应用**
   ```bash
   mvn spring-boot:run
   ```

6. **测试接口**
   ```bash
   # 阻塞式聊天
   curl "http://localhost:8080/chat/ai/dashscope?question=你好"
   
   # 流式聊天
   curl "http://localhost:8080/chat/ai/dashscope/stream?question=介绍下Java"
   
   # 工具调用
   curl "http://localhost:8080/chat/ai/dashscope/add/date/time/tool/answer/user/questions?question=现在几点了"
   ```

---

## 📝 使用示例

### 示例 1: 基础聊天

```bash
GET /chat/ai/dashscope?question=什么是 Spring AI?
```

### 示例 2: 流式响应

```bash
GET /chat/ai/dashscope/stream?question=用 100 字介绍人工智能
```

响应格式 (SSE):

```
event: start
data: {"code":"200","success":true,"msg":"响应开始","data":""}

event: data
data: {"code":"200","success":true,"msg":"响应中...","data":"人工..."}

event: data
data: {"code":"200","success":true,"msg":"响应中...","data":"智能..."}

event: end
data: {"code":"200","success":true,"msg":"响应结束","data":""}
```

### 示例 3: 带会话记忆

```bash
# 第一次对话
GET /chat/ai/question/stream/chat/memory?question=我叫小明&conversationId=conv001

# 第二次对话（AI 能记住名字）
GET /chat/ai/question/stream/chat/memory?question=我叫什么名字？&conversationId=conv001
```

### 示例 4: 结构化输出

```bash
GET /chat/ai/topic/dashscope/struct/output/entity/call?topic=Java编程
```

响应:

```json
{
  "code": "200",
  "success": true,
  "msg": "success",
  "data": {
    "topic": "Java编程",
    "books": [
      "Effective Java",
      "Java 核心技术",
      "深入理解 Java 虚拟机"
    ]
  }
}
```

### 示例 5: 工具调用

```bash
# 获取当前时间
GET /chat/ai/dashscope/add/date/time/tool/answer/user/questions?question=现在北京时间几点?

# 设置闹钟（格式：yyyy-MM-dd HH:mm:ss）
GET /chat/ai/dashscope/add/date/time/tool/answer/user/questions?question=请设置一个今天下午3点的闹钟
```

### 示例 6: 文本嵌入

```bash
# 单文本向量化
GET /chat/ai/dashscope/embedding/convert/text?text=人工智能是未来的发展趋势

# 多文本向量化 + 距离计算 (type: 1=欧式距离, 2=余弦距离)
GET /chat/ai/dashscope/embedding/convert/multi/text?texts=人工智能是未来&texts=机器学习很重要&type=1
```

响应（单文本）:

```json
{
  "code": "200",
  "success": true,
  "msg": "success",
  "data": "[0.0123, -0.0456, 0.0789, ...]"
  // 2048 维向量
}
```

响应（多文本 + 距离计算）:

```json
{
  "code": "200",
  "success": true,
  "msg": "success",
  "data": "[{\"text1\":\"人工智能是未来\",\"text2\":\"机器学习很重要\",\"distance\":1.234,\"type\":\"EUCLIDEAN\"}]"
}
```

### 示例 7: 向量存储

```bash
# 存储文本到 Redis 向量库
GET /chat/ai/vector/store/user/input/text?texts=Spring AI 很强大&texts=Java 21 虚拟线程

# 相似度搜索（返回最相似的 2 条文档）
GET /chat/ai/vector/similarity/query/user/input/text?text=AI 框架
```

响应（存储）:

```json
{
  "code": "200",
  "success": true,
  "msg": "success",
  "data": "Success"
}
```

响应（相似度搜索）:

```json
{
  "code": "200",
  "success": true,
  "msg": "success",
  "data": "[{\"id\":\"doc-123\",\"text\":\"Spring AI 很强大\",\"score\":0.85}]"
}
```

### 示例 8: 向量文档管理

```bash
# 查询所有文档 ID
GET /chat/ai/vector/store/all/documents/id

# 删除指定文档
GET /chat/ai/vector/store/delete/document/id?id=doc-123

# 删除所有文档（谨慎操作）
GET /chat/ai/vector/store/delete/all/documents
```

---

## 🏗️ 架构设计

### ChatClient 配置

项目配置了 4 个独立的 `ChatClient` Bean、1 个 `OpenAiEmbeddingModel` 和 2 个向量存储对象：

1. **dashscopeChatClient**: 基础 DashScope 聊天客户端（带日志拦截器）
2. **ollamaChatClient**: Ollama 本地模型客户端（带日志拦截器）
3. **javaCounselorChatClient**: 专用 Java 技术咨询客户端（带系统提示词 + 日志拦截器）
    - 系统提示词：仅回答 Java 技术问题，拒绝非技术问题
    - 回答格式：一句话概括 → 代码示例 → 注意事项
4. **dashscopeInMemoryChatMemoryChatClient**: 带会话记忆的 DashScope 客户端（日志拦截器 + 记忆拦截器）
5. **openAiEmbeddingModel**: 文本嵌入模型（text-embedding-v4，2048 维）
6. **vectorStore**: 通用向量存储（`VectorStore` 接口，基于 Redis 实现）
7. **redisVectorStore**: Redis 向量存储（`RedisVectorStore` 实现，提供 count、delete 等扩展功能）

### 请求处理流程

```
用户请求 → ChatApi (Controller) 
         → ChatService (Service) 
         → ChatClient (AI 客户端)
         → Advisor (AOP 增强)
         → OpenAiChatModel / OllamaChatModel
         → 大模型 API
         → 响应处理
         → 返回给用户
```

---

## 🐛 注意事项

1. **环境变量生效**: 配置环境变量后需要重启终端和 IDE
2. **模型可用性**: 确保配置的模型名称在对应平台中存在
3. **网络访问**: DashScope 需要能访问 `dashscope.aliyuncs.com`
4. **Ollama 服务**: 使用 Ollama 前需确保本地服务已启动（默认端口 11434）
5. **快照版本**: 项目使用 Spring Boot 快照版本，需要配置 Spring Snapshots 仓库
6. **会话记忆**: 当前使用内存存储（`InMemoryChatMemoryRepository`），应用重启后会话数据会丢失
7. **闹钟功能**: 需要网络连接以播放在线音频（依赖 JLayer 库）
8. **Embedding 向量**: 多文本接口至少需要传入 2 个文本才能计算距离
9. **距离计算**: 向量维度不一致时返回 -1.0，余弦距离范围 [0, 2]（值越小越相似）
10. **虚拟线程调度器**: 使用单线程调度器 + 虚拟线程执行器模式，避免阻塞平台线程
11. **Redis 向量数据库**: 需要安装 Redis Stack（包含 RedisSearch 模块），当前配置连接 192.168.31.83:16380
12. **向量索引初始化**: 启动时会自动创建向量索引（`initialize-schema: true`），所有向量默认存储在 0 号数据库
13. **批量删除优化**: 删除所有文档时采用分批删除策略，避免一次性删除大量数据导致性能问题

---

## 📚 学习资源

- [Spring AI 官方文档](https://docs.spring.io/spring-ai/reference/)
- [阿里云百炼平台文档](https://help.aliyun.com/zh/model-studio/)
- [Ollama 官方文档](https://ollama.ai/docs)
- [Spring Boot 文档](https://docs.spring.io/spring-boot/docs/current/reference/html/)

---

## 👨‍💻 开发者

- **作者**: ranyk
- **邮箱**: 13547180017@163.com
- **GitHub**: https://github.com/Utomcat
- **创建日期**: 2026-06-22
- **仓库地址**: https://github.com/Utomcat/spring-ai-study

---

## 📄 许可证

Apache License, Version 2.0

本项目仅用于学习和演示目的。

---

**🎉 祝您使用愉快！如有问题欢迎反馈。**
