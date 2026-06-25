package com.ranyk.spring.ai.base.domain.mapstruct.file.query;

import com.ranyk.spring.ai.base.domain.dto.QuerySimilarityFileDTO;
import com.ranyk.spring.ai.base.domain.request.QuerySimilarityFileRequest;
import org.mapstruct.Mapper;

/**
 * CLASS_NAME: QuerySimilarityFileMapper.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 相似文件内容查询转换 MapStruct 接口
 * @date: 2026-06-25
 */
@SuppressWarnings("unused")
@Mapper(componentModel = "spring")
public interface QuerySimilarityFileMapper {

    /**
     * 将 查询相似文件内容请求参数 {@link QuerySimilarityFileRequest} 对象 转换为 查询相似文件内容数据传输对象 {@link QuerySimilarityFileDTO} 对象
     *
     * @param querySimilarityFileRequest 查询相似文件内容请求参数 {@link QuerySimilarityFileRequest}
     * @return 查询相似文件内容数据传输对象 {@link QuerySimilarityFileDTO}
     */
    QuerySimilarityFileDTO querySimilarityFileRequestToQuerySimilarityFileDTO(QuerySimilarityFileRequest querySimilarityFileRequest);
}
