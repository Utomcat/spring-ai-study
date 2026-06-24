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

- **获取当前时间**: 根据用户时区返回实时时间
- **设置闹钟**: 在指定时间触发提醒（支持音频播放）

### 7. AOP 增强 (Advisor)

自定义 `MySimpleLoggerAdvisor` 拦截器：

- 记录阻塞式和流式请求的完整日志
- 监控请求和响应内容
- 便于调试和性能分析

### 8. 虚拟线程

使用 Java 21 虚拟线程处理异步任务：

- 延迟任务调度
- 高并发场景优化

---

## 🔧 API 接口文档

所有接口基础路径: `/chat/ai`

### DashScope 模型接口

| 接口路径                                                  | 方法  | 说明                | 参数                           |
|-------------------------------------------------------|-----|-------------------|------------------------------|
| `/dashscope`                                          | GET | 阻塞式聊天             | `question`                   |
| `/dashscope/stream`                                   | GET | 流式聊天 (SSE)        | `question`                   |
| `/topic/dashscope/stream`                             | GET | 提示词模板流式           | `topic`                      |
| `/topic/dashscope/system/stream`                      | GET | System+User 提示词流式 | `topic`                      |
| `/topic/dashscope/system/stream/defaultSystem`        | GET | 默认系统提示词流式         | `topic`                      |
| `/topic/dashscope/struct/output/entity/call`          | GET | 结构化输出-书籍推荐        | `topic`                      |
| `/topic/dashscope/struct/output/entity/list/call`     | GET | 结构化输出-书评列表        | `bookName`                   |
| `/question/stream/chat/memory`                        | GET | 带会话记忆的流式聊天        | `question`, `conversationId` |
| `/dashscope/add/date/time/tool/answer/user/questions` | GET | 工具调用-回答问题         | `question`                   |

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
    ollama:
      base-url: http://localhost:11434
      chat:
        model: ${OLLAMA_LLM_MODEL:deepseek-r1:1.5b}
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
GET /chat/ai/dashscope/add/date/time/tool/answer/user/questions?question=现在北京时间几点?
```

---

## 🏗️ 架构设计

### ChatClient 配置

项目配置了 4 个独立的 `ChatClient` Bean：

1. **dashscopeChatClient**: 基础 DashScope 聊天客户端
2. **ollamaChatClient**: Ollama 本地模型客户端
3. **javaCounselorChatClient**: 专用 Java 技术咨询客户端（带系统提示词）
4. **dashscopeInMemoryChatMemoryChatClient**: 带会话记忆的 DashScope 客户端

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
4. **Ollama 服务**: 使用 Ollama 前需确保本地服务已启动
5. **快照版本**: 项目使用 Spring Boot 快照版本，需要配置 Spring Snapshots 仓库

---

## 📚 学习资源

- [Spring AI 官方文档](https://docs.spring.io/spring-ai/reference/)
- [阿里云百炼平台文档](https://help.aliyun.com/zh/model-studio/)
- [Ollama 官方文档](https://ollama.ai/docs)
- [Spring Boot 文档](https://docs.spring.io/spring-boot/docs/current/reference/html/)

---

## 👨‍💻 开发者

- **作者**: ranyk
- **创建日期**: 2026-06-22

---

## 📄 许可证

本项目仅用于学习和演示目的。

---

**🎉 祝您使用愉快！如有问题欢迎反馈。**
