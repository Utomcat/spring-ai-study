package com.ranyk.spring.ai.base.domain.request;

import java.io.Serializable;

/**
 * CLASS_NAME: QuerySimilarityFileRequest.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 查询文件中存在相似信息的文件内容请求参数封装 Record 对象, 其字段说明如下:
 * <ul>
 *     <li>documentId: 文件的唯一标识符</li>
 *     <li>source: 文件的存储路径</li>
 *     <li>fileName: 文件名</li>
 *     <li>category: 文件类别</li>
 *     <li>uploader: 文件上传者</li>
 *     <li>text: 需查询的相似内容</li>
 * </ul>
 * @date: 2026-06-25
 */
public record QuerySimilarityFileRequest(String documentId, String source, String fileName, String category, String uploader, String text) implements Serializable {
}
