package com.ranyk.spring.ai.demo.domain.request;

import cn.hutool.core.util.StrUtil;

import java.io.Serializable;

/**
 * CLASS_NAME: UploaderRequest.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 文件上传请求基础信息数据封装 Record, 其字段说明如下:
 * <ul>
 *     <li>category: 文件上传的类别</li>
 *     <li>uploader: 文件上传者</li>
 * </ul>
 * @date: 2026-06-25
 */
@SuppressWarnings("unused")
public record UploaderRequest(String category, String uploader) implements Serializable {

    public UploaderRequest {
        if (StrUtil.isBlank(category)) {
            category = "default";
        }
        if (StrUtil.isBlank(uploader)) {
            uploader = "anonymous";
        }
    }

    public static UploaderRequest of(String category, String uploader) {
        return new UploaderRequest(category, uploader);
    }

    public static UploaderRequest empty() {
        return new UploaderRequest(null, null);
    }
}
