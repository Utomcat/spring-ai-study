package com.ranyk.spring.ai.base.domain.vo;

import java.io.Serializable;

/**
 * CLASS_NAME: TopicBookReview.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 返回大模型通过思考后返回的书评信息数据封装对象, 其字段如下:
 * <ul>
 *     <li>user: 评价用户</li>
 *     <li>score: 评分</li>
 *     <li>content: 书评内容</li>
 * </ul>
 * @date: 2026-06-23
 */
public record TopicBookReview(String user, int score, String content) implements Serializable {
}
