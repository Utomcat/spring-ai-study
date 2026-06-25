package com.ranyk.spring.ai.demo.domain.dto;

import lombok.*;

/**
 * CLASS_NAME: UploaderDTO.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 系统内文件上传基础信息数据传入DTO 类
 * @date: 2026-06-25
 */
@Data
@Builder
@ToString
@NoArgsConstructor
@EqualsAndHashCode
@AllArgsConstructor
public class UploaderDTO {
    /**
     * 文件上传类别
     */
    private String category;
    /**
     * 文件上传者
     */
    private String uploader;
}
