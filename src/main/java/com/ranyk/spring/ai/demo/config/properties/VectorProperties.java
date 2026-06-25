package com.ranyk.spring.ai.demo.config.properties;

import lombok.*;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * CLASS_NAME: VectorProperties.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 向量配置属性类
 * @date: 2026-06-25
 */
@Data
@Component
@NoArgsConstructor
@AllArgsConstructor
@ConfigurationProperties(prefix = VectorProperties.CONFIG_PREFIX)
public class VectorProperties {
    /**
     * 自定义 - 向量配置属性前缀
     */
    public static final String CONFIG_PREFIX = "vector";

    /**
     * 自定义 - 向量数据属性
     */
    private VectorData vectorData;

    /**
     * 自定义 - 向量数据属性类
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class VectorData {
        /**
         * 自定义 - 向量数据删除属性
         */
        private Delete delete;

        /**
         * 自定义 - 向量数据删除属性类
         */
        @Data
        @NoArgsConstructor
        @AllArgsConstructor
        public static class Delete {
            /**
             * 自定义 - 向量数据删除每批次数量
             */
            private Integer batchQuantity;
        }
    }
}
