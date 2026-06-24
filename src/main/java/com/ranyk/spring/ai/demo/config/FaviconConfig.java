package com.ranyk.spring.ai.demo.config;

import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * CLASS_NAME: FaviconConfig.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 图标资源配置类
 * @date: 2026-06-22
 */
@Slf4j
@Configuration
public class FaviconConfig implements WebMvcConfigurer {

    /**
     * 添加资源处理器
     *
     * @param registry 资源处理器
     */
    @Override
    public void addResourceHandlers(@NonNull ResourceHandlerRegistry registry) {
        Resource resource = new ClassPathResource("static/favicon.ico");
        if (resource.exists()) {
            log.info("Favicon resource exists.");
            registry.addResourceHandler("/favicon.ico")
                    .addResourceLocations("classpath:/static/")
                    .setCachePeriod(3600);
        }
    }
}
