package com.ranyk.spring.ai.demo.domain.mapstruct;

import com.ranyk.spring.ai.demo.domain.dto.UploaderDTO;
import com.ranyk.spring.ai.demo.domain.request.UploaderRequest;
import org.mapstruct.Mapper;

/**
 * CLASS_NAME: UploaderMapper.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 文件上传基础数据转换 MapStruct 接口
 * @date: 2026-06-25
 */
@Mapper(componentModel = "spring")
public interface UploaderMapper {

    /**
     * 将文件上传请求参数对象 转换为 文件上传数据传输对象
     *
     * @param uploader 文件上传请求参数对象 {@link UploaderRequest}
     * @return 文件上传数据传输对象 {@link UploaderDTO}
     */
    UploaderDTO uploaderRequestToUploaderDTO(UploaderRequest uploader);
}
