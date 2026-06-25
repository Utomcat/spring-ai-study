package com.ranyk.spring.ai.demo.config;

import com.ranyk.spring.ai.demo.config.properties.FileProperties;
import com.ranyk.spring.ai.demo.config.properties.VectorProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * CLASS_NAME: ApplicationConfiguration.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 应用配置类, 用于配置应用自定义的一些配置 Bean 或进行加载应用所需的配置属性
 * @date: 2026-06-25
 */
@Configuration
@EnableConfigurationProperties(value = {VectorProperties.class, FileProperties.class})
public class ApplicationConfiguration {
}
