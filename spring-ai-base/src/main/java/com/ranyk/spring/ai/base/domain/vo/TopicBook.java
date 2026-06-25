package com.ranyk.spring.ai.base.domain.vo;

import java.io.Serializable;
import java.util.List;

/**
 * CLASS_NAME: TopicBook.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 返回大模型通过思考后返回的书籍数据封装对象, 其字段如下:
 * <ul>
 *     <li>topic: String 类型, 表示大模型思考的题目</li>
 *     <li>books: List&lt;String&gt; 类型, 表示大模型推荐的书籍列表</li>
 * </ul>
 * @date: 2026-06-23
 */
public record TopicBook(String topic, List<String> books) implements Serializable {
}
