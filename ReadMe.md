# spring-ai-study - Spring AI 大模型集成学习项目

## 📖 项目简介

spring-ai-study 是一个基于 **Spring Boot 4.1.1** 和 **Spring AI 2.0.0** 构建的大语言模型集成学习项目。该项目采用**多模块架构**，演示了如何使用
Spring AI 框架集成多种大模型服务（阿里云百炼平台 qwen3.7-plus 和本地 Ollama deepseek-r1:1.5b），并展示了流式响应、会话记忆、结构化输出、工具调用、
**MCP Server 服务端、文本嵌入、向量存储、文件上传解析、RAG 检索增强生成**等高级 AI 功能。

### 🎯 核心能力

- **多模型支持**: 阿里云 DashScope（qwen3.7-plus）+ 本地 Ollama（deepseek-r1:1.5b）
- **智能对话**: 阻塞式/流式聊天、会话记忆、提示词工程、结构化输出
- **工具调用**: AI 可调用 Java 方法（获取时间、设置闹钟等）
- **MCP Server**: 提供标准化的工具服务端，支持 AI 模型通过 MCP 协议调用工具（天气预报等）
- **文本嵌入**: 使用 text-embedding-v4 模型进行文本向量化和相似度计算
- **向量存储**: 基于 Redis Stack 的向量数据库，支持相似度搜索和文档管理
- **文件处理**: 支持 PDF/Word/TXT/Markdown 文件上传、解析、分块和向量化
- **RAG 增强**: 基于私有知识库的检索增强生成，提高回答准确性
- **虚拟线程**: Java 21 虚拟线程处理异步任务和定时调度
- **国际化**: 支持中英文多语言消息

### 📦 项目模块

| 模块名称               | 说明                                      | 包名                              | 端口  |
|--------------------|-----------------------------------------|---------------------------------|-----|
| spring-ai-study    | 父项目（POM），管理依赖版本和构建配置                    | -                               | -   |
| spring-ai-base     | Spring AI 基础知识模块，包含所有核心功能实现             | com.ranyk.spring.ai.base        | 8080|
| spring-ai-mcp-server | Spring AI MCP Server 模块，提供 MCP 工具服务端 | com.ranyk.spring.ai.mcp.server | 8081|

### 🏗️ 项目架构

本项目采用 **Maven 多模块架构**：

- **父项目 (spring-ai-study)**:
    - 统一管理依赖版本（通过 `<dependencyManagement>`）
    - 统一构建配置（编译器插件、资源处理等）
    - packaging 类型为 `pom`

- **子模块 (spring-ai-base)**:
    - 继承父项目的依赖版本
    - 包含所有业务代码和资源配置
    - 可独立运行和测试
    - 端口：8080

- **子模块 (spring-ai-mcp-server)**:
    - Spring AI MCP Server 实现
    - 提供可被 MCP Client 调用的工具服务
    - 端口：8081
    - 通过 SSE 与 spring-ai-base 通信

---

## 🚀 技术栈

| 技术                             | 版本             | 说明                 |
|--------------------------------|----------------|--------------------|
| Java                           | 21             | 使用虚拟线程等现代特性        |
| Spring Boot                    | 4.1.1-SNAPSHOT | 应用框架               |
| Spring AI                      | 2.0.0          | AI 集成框架            |
| Lombok                         | 1.18.46        | 简化代码               |
| MapStruct                      | 1.7.0.Beta1    | 对象映射               |
| Hutool                         | 5.8.46         | Java 工具库           |
| JLayer                         | 1.0.1          | MP3 音频播放           |
| Redis Stack                    | Latest         | 向量数据库（RedisSearch） |
| Apache Tika                    | Latest         | 文档解析（PDF/Word/TXT） |
| Spring AI Tika Reader          | Latest         | Tika 文档读取器         |
| Spring AI Markdown Reader      | Latest         | Markdown 文档读取器     |
| Spring AI Redis Vector Store   | Latest         | Redis 向量存储         |
| Spring AI Vector Store Advisor | Latest         | RAG 检索增强           |

---

## 📁 项目结构

```
spring-ai-study/                              # 父项目（POM）
├── pom.xml                                   # 父项目 POM，管理依赖版本
├── spring-ai-base/                           # Spring AI 基础知识模块
│   ├── pom.xml                               # 子模块 POM
│   └── src/
│       ├── main/
│       │   ├── java/com/ranyk/spring/ai/base/
│       │   │   ├── SpringAiBaseApplication.java      # 应用启动类
│       │   │   ├── api/
│       │   │   │   └── ChatApi.java                  # 聊天 REST API 接口（包含文件上传、向量检索等）
│       │   │   ├── service/
│       │   │   │   ├── ChatService.java              # 聊天业务逻辑处理（包含向量存储、文件解析等）
│       │   │   │   └── DelayedTaskService.java       # 延迟任务服务（虚拟线程调度）
│       │   │   ├── config/
│       │   │   │   ├── AdvisorConfiguration.java            # Advisor 增强器配置
│       │   │   │   ├── ApplicationConfiguration.java        # 应用基础配置
│       │   │   │   ├── ChatClientConfiguration.java         # ChatClient Bean 配置（4个客户端）
│       │   │   │   ├── ChatMemoryConfiguration.java         # 会话记忆配置
│       │   │   │   ├── DocumentSplitterConfiguration.java   # 文档分块器配置
│       │   │   │   ├── EmbeddingModelConfiguration.java     # 嵌入模型配置
│       │   │   │   ├── FaviconConfiguration.java            # 网站图标配置
│       │   │   │   ├── I18nConfiguration.java               # 国际化配置
│       │   │   │   ├── RedisAndRedisVectorStoreConfiguration.java # Redis 配置
│       │   │   │   ├── VectorStoreConfiguration.java        # 向量存储配置
│       │   │   │   ├── VirtualThreadConfiguration.java      # 虚拟线程配置
│       │   │   │   └── properties/
│       │   │   │       ├── FileProperties.java              # 文件上传配置属性
│       │   │   │       └── VectorProperties.java            # 向量存储配置属性
│       │   │   ├── advisor/
│       │   │   │   └── MySimpleLoggerAdvisor.java           # 自定义请求/响应日志拦截器
│       │   │   ├── ai/tool/
│       │   │   │   └── DataTimeTool.java                    # AI 可调用的时间工具类（获取时间、设置闹钟）
│       │   │   ├── common/
│       │   │   │   ├── constant/
│       │   │   │   │   └── FileTypeEnum.java                # 文件类型枚举
│       │   │   │   ├── domain/
│       │   │   │   │   ├── metadata/
│       │   │   │   │   │   └── VectorDataMetaData.java      # 向量数据元数据
│       │   │   │   │   └── vo/
│       │   │   │   │       └── Result.java                  # 统一响应结果封装
│       │   │   │   └── exception/
│       │   │   │       ├── BaseException.java               # 基础异常类
│       │   │   │       └── ServiceException.java            # 业务异常类
│       │   │   ├── domain/
│       │   │   │   ├── dto/
│       │   │   │   │   ├── QuerySimilarityFileDTO.java      # 相似度查询 DTO
│       │   │   │   │   └── UploaderDTO.java                 # 文件上传 DTO
│       │   │   │   ├── mapstruct/file/
│       │   │   │   │   ├── query/
│       │   │   │   │   │   └── QuerySimilarityFileMapper.java # 查询映射器
│       │   │   │   │   └── upload/
│       │   │   │   │       └── UploaderMapper.java          # 上传映射器
│       │   │   │   ├── request/
│       │   │   │   │   ├── QuerySimilarityFileRequest.java  # 相似度查询请求
│       │   │   │   │   └── UploaderRequest.java             # 文件上传请求
│       │   │   │   └── vo/
│       │   │   │       ├── TopicBook.java                   # 书籍推荐结构化输出 VO
│       │   │   │       └── TopicBookReview.java             # 书评结构化输出 VO
│       │   │   ├── handle/
│       │   │   │   └── GlobalWebExceptionHandler.java       # 全局 Web 异常处理器
│       │   │   └── utils/
│       │   │       ├── DocumentParseUtils.java              # 文档解析工具类（支持 PDF/Word/TXT/MD）
│       │   │       ├── MathUtils.java                       # 数学工具类（距离计算）
│       │   │       └── MessageUtils.java                    # 消息工具类
│       │   └── resources/
│       │       ├── application.yml                          # 应用配置文件
│       │       ├── i18n/
│       │       │   ├── messages.properties                  # 默认消息
│       │       │   ├── messages_en_US.properties            # 英文消息
│       │       │   └── messages_zh_CN.properties            # 中文消息
│       │       └── static/
│       │           └── favicon.ico                          # 网站图标
│       └── test/
│           └── java/com/ranyk/spring/ai/base/
│               └── SpringAiBaseApplicationTests.java        # 测试类
├── spring-ai-mcp-server/                     # Spring AI MCP Server 模块
│   ├── pom.xml                               # 子模块 POM
│   └── src/
│       └── main/
│           ├── java/com/ranyk/spring/ai/mcp/server/
│           │   ├── SpringAiMcpServerApplication.java       # MCP Server 启动类
│           │   └── ai/
│           │       └── tool/
│           │           └── WeatherMcpTool.java             # MCP 工具示例（天气预报）
│           └── resources/
│               └── application.yml                          # MCP Server 配置文件
├── .gitignore                                # Git 忽略配置
├── LICENSE                                   # 许可证文件
└── ReadMe.md                                 # 项目说明文档
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

### 11. 文件上传与智能解析 (File Upload & Parsing)

支持多种文件格式的上传、解析和向量化存储：

- **支持的文件格式**:
    - **PDF 文档**: 使用 TikaDocumentReader 解析
    - **Word 文档** (.docx): 使用 TikaDocumentReader 解析
    - **文本文件** (.txt): 使用 TikaDocumentReader 解析
    - **Markdown 文件** (.md): 使用 MarkdownDocumentReader 解析
- **文件处理流程**:
    1. **文件接收**: 接收 MultipartFile 数组，过滤空文件
    2. **文件保存**: 保存到 `upload/{timestamp}/` 目录（可配置根目录）
    3. **文档解析**: 根据文件扩展名自动选择解析器
    4. **元数据注入**: 为每个 Document 添加元数据（documentId、source、fileName、category、uploader）
    5. **文档分块**: 使用 TokenTextSplitter 按 Token 拆分大文档
    6. **向量存储**: 将分块后的文档存储到 Redis 向量数据库
- **元数据管理** (`VectorDataMetaData`):
    - `documentId`: 唯一文档标识（UUID）
    - `source`: 文件保存路径
    - `fileName`: 原始文件名
    - `category`: 文件分类（用户指定）
    - `uploader`: 上传者信息（用户指定）
- **配置项**:
    - `file.upload.root`: 文件上传根目录（默认 `${user.dir}/upload`）
    - `spring.servlet.multipart.max-file-size`: 单个文件最大大小（默认 50MB）
    - `spring.servlet.multipart.max-request-size`: 请求最大大小（默认 200MB）

### 12. 文档分块处理 (Document Splitting)

使用 `TokenTextSplitter` 对文档进行智能分块：

- **分块策略**: 基于 Token 数量进行拆分，保持语义完整性
- **配置参数**:
    - `defaultChunkSize`: 默认块大小（Token 数量）
    - `minChunkSizeChars`: 最小块字符数
    - `minChunkLengthToEmbed`: 最小嵌入长度
    - `maxNumChunks`: 最大块数量
    - `keepSeparator`: 是否保留分隔符
- **应用场景**: 处理长文档，避免超出模型上下文窗口限制，提升检索精度

### 13. RAG 检索增强生成 (Retrieval-Augmented Generation)

使用 `QuestionAnswerAdvisor` 实现智能问答增强：

- **工作原理**:
    1. 用户提出问题
    2. 从向量数据库中检索最相关的文档片段
    3. 将检索到的上下文注入提示词
    4. 大模型基于上下文生成准确回答
- **配置参数**:
    - `similarityThreshold`: 相似度阈值 [0, 1]，过滤低相关性结果（默认 0.7）
    - `topK`: 检索的文档数量（默认 1）
- **优势**:
    - 提高回答准确性，减少幻觉
    - 基于私有知识库回答
    - 可追溯答案来源
- **应用场景**: 智能客服、知识库问答、文档检索助手

### 14. 国际化支持 (i18n)

支持多语言国际化消息：

- **支持的语言**:
    - 简体中文 (`zh_CN`)
    - 英文 (`en_US`)
    - 默认语言 (`messages.properties`)
- **配置项**:
    - `spring.web.locale`: 默认语言环境（默认 `zh_CN`）
    - `spring.web.locale-resolver`: 语言解析器（默认 `accept-header`）
- **消息文件位置**: `src/main/resources/i18n/`
- **使用场景**: 异常消息、业务提示、国际化响应

### 15. MCP Server 服务 (Model Context Protocol)

提供 MCP Server 实现,支持 AI 模型通过标准化协议调用工具：

- **MCP Server 配置**:
    - 服务名称: `spring-ai-mcp-server`
    - 服务版本: `1.0.0`
    - 服务类型: `SYNC` (同步模式)
    - 端口: `8081`
    - SSE 连接: 与 spring-ai-base 模块通过 SSE 通信

- **已提供的 MCP 工具**:
    - `getWeatherForecast`: 根据城市名称查询天气预报（演示数据）
        - 参数: `city` (城市名称)
        - 返回: 天气预报信息字符串

- **配置项**:
    - `spring.ai.mcp.server.name`: MCP Server 名称
    - `spring.ai.mcp.server.version`: MCP Server 版本
    - `spring.ai.mcp.server.type`: 服务类型 (SYNC/ASYNC)
    - `spring.ai.mcp.server.annotation-scanner.enabled`: 是否启用注解扫描

- **MCP Client 配置** (spring-ai-base 模块):
    - 连接地址: `http://localhost:8081`
    - 连接类型: SSE (Server-Sent Events)
    - 启用状态: 已启用

- **工作流程**:
    1. spring-ai-base 模块作为 MCP Client 连接 spring-ai-mcp-server
    2. AI 模型识别用户意图后通过 MCP 协议调用工具
    3. spring-ai-mcp-server 执行工具方法并返回结果
    4. AI 模型基于工具返回结果生成回答

- **优势**:
    - 标准化的工具调用协议
    - 工具服务独立部署,便于扩展
    - 支持多语言、多框架集成
    - 工具可复用,一次部署多处使用

- **应用场景**: 天气查询、外部 API 调用、数据检索、业务逻辑封装

---

## 🔧 API 接口文档

所有接口基础路径: `/chat/ai`

### DashScope 模型接口

| 接口路径                                                                | 方法   | 说明                 | 参数                                                |
|---------------------------------------------------------------------|------|--------------------|---------------------------------------------------|
| `/dashscope`                                                        | GET  | 阻塞式聊天              | `question`                                        |
| `/dashscope/stream`                                                 | GET  | 流式聊天 (SSE)         | `question`                                        |
| `/topic/dashscope/stream`                                           | GET  | 提示词模板流式            | `topic`                                           |
| `/topic/dashscope/system/stream`                                    | GET  | System+User 提示词流式  | `topic`                                           |
| `/topic/dashscope/system/stream/defaultSystem`                      | GET  | 默认系统提示词流式（Java 顾问） | `topic`                                           |
| `/topic/dashscope/struct/output/entity/call`                        | GET  | 结构化输出-书籍推荐         | `topic`                                           |
| `/topic/dashscope/struct/output/entity/list/call`                   | GET  | 结构化输出-书评列表         | `bookName`                                        |
| `/question/stream/chat/memory`                                      | GET  | 带会话记忆的流式聊天         | `question`, `conversationId`                      |
| `/dashscope/add/date/time/tool/answer/user/questions`               | GET  | 工具调用-回答问题（时间/闹钟）   | `question`                                        |
| `/dashscope/embedding/convert/text`                                 | GET  | 文本嵌入-单文本向量化        | `text`                                            |
| `/dashscope/embedding/convert/multi/text`                           | GET  | 文本嵌入-多文本向量化+距离计算   | `texts`, `type`                                   |
| `/vector/store/user/input/text`                                     | GET  | 向量存储-存储文本到Redis向量库 | `texts` (List)                                    |
| `/vector/similarity/query/user/input/text`                          | GET  | 向量查询-相似度搜索         | `text`                                            |
| `/vector/store/all/documents/id`                                    | GET  | 向量管理-获取所有文档ID      | 无                                                 |
| `/vector/store/delete/document/id`                                  | GET  | 向量管理-删除指定文档        | `id`                                              |
| `/vector/store/delete/all/documents`                                | GET  | 向量管理-删除所有文档        | 无                                                 |
| `/upload/file/vector/store`                                         | POST | 文件上传并存储到向量库        | `files` (MultipartFile[]), `category`, `uploader` |
| `/similarity/search/vector/store/file`                              | GET  | 文件相似度搜索            | `text`, `topK` (可选)                               |
| `/similarity/search/vector/store/file/with/question/answer/advisor` | GET  | RAG增强文件相似度问答       | `text`                                            |

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

**应用配置文件位置**: `spring-ai-base/src/main/resources/application.yml`

```yaml
spring:
  application:
    name: spring-ai-demo

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

  web:
    locale: zh_CN
    locale-resolver: accept-header

  servlet:
    multipart:
      max-file-size: 50MB
      max-request-size: 200MB
      file-size-threshold: 2KB

# 自定义向量配置
vector:
  data:
    delete:
      batch-quantity: 100

# 文件上传配置
file:
  upload:
    root: ${user.dir}/upload

# 日志配置
logging:
  level:
    org.springframework.ai: debug
    com.ranyk.spring.ai.base: debug

# 服务器配置
server:
  port: 8080
```

---

## 🏃 快速开始

### 前置要求

1. **Java 21+** 已安装
2. **Maven 3.8+** 已安装
3. 阿里云百炼平台 API 密钥（如需使用 DashScope）
4. 本地安装 Ollama（如需使用本地模型）
5. **Redis Stack** 已安装并启动（用于向量存储，包含 RedisSearch 模块）

#### Redis Stack 安装（Docker 方式）

```bash
# 拉取 Redis Stack 镜像
docker pull redis/redis-stack:latest

# 运行 Redis Stack 容器
docker run -d \
  --name redis-stack \
  -p 6379:6379 \
  -p 8001:8001 \
  redis/redis-stack:latest

# 验证安装
docker exec -it redis-stack redis-cli ping
# 应返回 PONG

# 访问 RedisInsight UI（可选）
# 浏览器打开 http://localhost:8001
```

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
   # 在父项目根目录执行
   mvn clean install
   ```

5. **运行应用**
   ```bash
   # 方式一：在父项目根目录，同时启动两个模块
   # 先启动 MCP Server
   cd spring-ai-mcp-server
   mvn spring-boot:run

   # 再启动主应用
   cd ../spring-ai-base
   mvn spring-boot:run

   # 方式二：在 IDE 中运行
   # 1. 先运行 SpringAiMcpServerApplication.java (端口 8081)
   # 2. 再运行 SpringAiBaseApplication.java (端口 8080)

   # 方式三：仅启动 spring-ai-base 模块（不使用 MCP 功能）
   cd spring-ai-base
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

### 示例 9: 文件上传与向量化

```bash
# 上传文件并自动解析存储到向量库（支持 PDF/Word/TXT/MD）
POST /chat/ai/upload/file/vector/store
Content-Type: multipart/form-data

files: [选择多个文件]
category: 技术文档
uploader: ranyk
```

响应:

```json
{
  "code": "200",
  "success": true,
  "msg": "success",
  "data": "Success"
}
```

**处理流程**:

1. 接收并验证文件（过滤空文件）
2. 保存到 `upload/{timestamp}/` 目录
3. 根据文件类型自动选择解析器（Tika/Markdown）
4. 提取文档内容并注入元数据（documentId、fileName、category 等）
5. 使用 TokenTextSplitter 按 Token 分块
6. 存储到 Redis 向量数据库

### 示例 10: 文件相似度搜索

```bash
# 基础相似度搜索（返回 top 5 相似文档）
GET /chat/ai/similarity/search/vector/store/file?text=Spring AI 向量存储

# RAG 增强问答（基于检索到的文档生成回答）
GET /chat/ai/similarity/search/vector/store/file/with/question/answer/advisor?text=如何使用 Redis 存储向量？
```

响应（基础搜索）:

```json
{
  "code": "200",
  "success": true,
  "msg": "success",
  "data": "[{\"id\":\"doc-uuid\",\"text\":\"Spring AI 支持 Redis 向量存储...\",\"score\":0.85,\"metadata\":{\"fileName\":\"技术文档.pdf\",\"category\":\"技术文档\"}}]"
}
```

响应（RAG 问答）:

```json
{
  "code": "200",
  "success": true,
  "msg": "success",
  "data": "使用 Redis 存储向量需要以下步骤：1. 配置 Redis Stack... [基于检索到的文档生成的回答]"
}
```

### 示例 11: MCP 工具调用（天气预报）

```bash
# 确保已启动 spring-ai-mcp-server (8081) 和 spring-ai-base (8080)
# AI 模型会自动识别用户意图，通过 MCP 协议调用 WeatherMcpTool 工具

# 通过自然语言询问天气（AI 会自动调用 getWeatherForecast 工具）
GET /chat/ai/dashscope?question=北京今天天气怎么样？
GET /chat/ai/dashscope?question=帮我查询上海的天气预报
```

响应:

```json
{
  "code": "200",
  "success": true,
  "msg": "success",
  "data": "根据查询结果，当前城市: 北京 的天气预报信息为: 天气很好, 温度适宜, 湿度适中, 风向: 东风, 风级: 3级, 最低温度: 22℃, 最高温度: 30℃"
}
```

**MCP 工具调用流程**:

1. 用户提出天气相关问题
2. AI 模型识别意图，判断需要调用 MCP 工具
3. 通过 SSE 连接调用 spring-ai-mcp-server 的 `getWeatherForecast` 工具
4. MCP Server 执行工具方法并返回结果
5. AI 模型基于工具返回结果生成自然语言回答
6. 返回给用户

**注意**: 当前天气预报为演示数据，真实场景需接入气象 API。

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

### 文档解析与分块架构

```
文件上传 → 文件类型识别 → 选择解析器 (Tika/Markdown)
         → 解析为 Document 列表
         → 注入元数据 (documentId, fileName, category, uploader)
         → TokenTextSplitter 分块
         → 存储到 Redis 向量数据库
```

### RAG 检索增强架构

```
用户提问 → QuestionAnswerAdvisor 拦截
         → 向量相似度搜索 (RedisVectorStore)
         → 检索 topK 相关文档 (similarityThreshold 过滤)
         → 将文档上下文注入提示词
         → DashScope 大模型生成回答
         → 返回基于知识库的准确答案
```

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

### 文件上传处理流程

```
MultipartFile[] → 过滤空文件
                → 保存到 upload/{timestamp}/ 目录
                → DocumentParseUtils.parse() 解析
                → 注入 VectorDataMetaData 元数据
                → TokenTextSplitter 分块
                → RedisVectorStore.add() 存储
                → 返回成功响应
```

### MCP Server 架构

```
spring-ai-base (MCP Client, 端口 8080)
    ↓ SSE 连接 (http://localhost:8081)
spring-ai-mcp-server (MCP Server, 端口 8081)
    ↓ 注解扫描
@McpTool 工具类 (WeatherMcpTool 等)
    ↓ 执行业务逻辑
返回结果给 MCP Client
    ↓
AI 模型生成回答
```

**MCP Server 模块结构**:
- `SpringAiMcpServerApplication`: 启动类，配置 MCP Server
- `WeatherMcpTool`: 天气预报工具示例
  - 使用 `@McpTool` 注解声明工具
  - 使用 `@McpToolParam` 注解声明参数
  - 提供 `getWeatherForecast` 方法供 MCP Client 调用

**通信协议**:
- SSE (Server-Sent Events): 实现服务端推送
- JSON-RPC 2.0: MCP 协议的基础消息格式
- 同步模式 (SYNC): 当前配置的服务类型

**扩展方式**:
1. 创建新的工具类并添加 `@Component` 注解
2. 使用 `@McpTool` 注解标记工具方法
3. 使用 `@McpToolParam` 注解标记参数
4. 重启 spring-ai-mcp-server 服务
5. spring-ai-base 自动发现并注册新工具

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
14. **文件上传限制**: 单个文件最大 50MB，整个请求最大 200MB，可在 `application.yml` 中调整
15. **文档解析支持**: 当前支持 PDF、Word (.docx)、TXT、Markdown 格式，其他格式会抛出异常
16. **文档分块策略**: 使用 TokenTextSplitter 按 Token 分块，保持语义完整性，避免超出模型上下文限制
17. **RAG 相似度阈值**: 默认 0.7，值越高要求越严格，可根据实际需求调整过滤低相关性结果
18. **元数据管理**: 每个向量文档都包含 documentId、source、fileName、category、uploader 元数据，便于检索和追溯
19. **国际化支持**: 默认语言为简体中文，支持英文切换，异常消息已做国际化处理
20. **文件存储路径**: 默认存储在 `${user.dir}/upload/` 目录，可通过 `file.upload.root` 配置修改
21. **MCP Server 端口**: spring-ai-mcp-server 默认端口 8081，需确保端口未被占用
22. **MCP 服务启动顺序**: 建议先启动 spring-ai-mcp-server (8081)，再启动 spring-ai-base (8080)，确保 MCP Client 能正常连接
23. **MCP SSE 连接**: spring-ai-base 通过 SSE 连接 MCP Server，确保两模块在同一网络或可互通
24. **MCP 工具扩展**: 新增 MCP 工具需在 spring-ai-mcp-server 模块中创建并添加 `@McpTool` 注解，重启服务生效
25. **模块独立运行**: spring-ai-base 和 spring-ai-mcp-server 可独立运行，但在使用 MCP 功能时需同时启动两个模块

---

## 📚 学习资源

- [Spring AI 官方文档](https://docs.spring.io/spring-ai/reference/)
- [Spring AI MCP 文档](https://docs.spring.io/spring-ai/reference/api/mcp)
- [阿里云百炼平台文档](https://help.aliyun.com/zh/model-studio/)
- [Ollama 官方文档](https://ollama.ai/docs)
- [Spring Boot 文档](https://docs.spring.io/spring-boot/docs/current/reference/html/)
- [Redis Stack 文档](https://redis.io/docs/stack/)
- [Apache Tika 文档](https://tika.apache.org/)
- [Java 21 虚拟线程](https://docs.oracle.com/en/java/javase/21/core/virtual-threads.html)
- [MCP 协议规范](https://modelcontextprotocol.io/)

---

## 👨‍💻 开发者

- **作者**: ranyk
- **邮箱**: 13547180017@163.com
- **GitHub**: https://github.com/Utomcat
- **创建日期**: 2026-06-22
- **仓库地址**: https://github.com/Utomcat/spring-ai-study
- **项目描述**: Spring AI 的学习项目

---

## 📄 许可证

Apache License, Version 2.0

本项目仅用于学习和演示目的。

---

**🎉 祝您使用愉快！如有问题欢迎反馈。**
