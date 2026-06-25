package com.ranyk.spring.ai.base.common.domain.metadata;

import lombok.Builder;

import java.io.Serializable;

/**
 * CLASS_NAME: VectorDataMetaData.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 定义当前文档向量存储的 MetaData 数据, 其字段说明如下:
 * <ul>
 *     <li>documentId: 文档的唯一标识符</li>
 *     <li>source: 文档的存放路径</li>
 *     <li>fileName: 文档的文件名</li>
 *     <li>category: 文档的类别</li>
 *     <li>uploader: 文档的上传者</li>
 * </ul>
 * @date: 2026-06-25
 */
@Builder
public record VectorDataMetaData(String documentId, String source, String fileName, String category, String uploader) implements Serializable {
}
