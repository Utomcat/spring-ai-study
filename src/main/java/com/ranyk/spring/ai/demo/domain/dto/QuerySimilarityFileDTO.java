package com.ranyk.spring.ai.demo.domain.dto;

import lombok.*;

import java.io.Serial;
import java.io.Serializable;

/**
 * CLASS_NAME: QuerySimilarityFileDTO.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 查询相似内容的请求数据系统内部数据传输 DTO 对象
 * @date: 2026-06-25
 */
@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class QuerySimilarityFileDTO  implements Serializable {
    @Serial
    private static final long serialVersionUID = -974963313066426465L;
    /**
     * 文件唯一 ID
     */
    private String documentId;
    /**
     * 文件来源
     */
    private String source;
    /**
     * 文件名
     */
    private String fileName;
    /**
     * 文件类别
     */
    private String category;
    /**
     * 上传者
     */
    private String uploader;
    /**
     * 需查询的相似内容
     */
    private String text;
}
