package com.ranyk.spring.ai.base.config;

import com.ranyk.spring.ai.base.advisor.MySimpleLoggerAdvisor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * CLASS_NAME: AdvisorConfiguration.java
 *
 * @author ranyk
 * @version V1.0
 * @description: ChatClient监管切面 Advisor 配置类
 * @date: 2026-06-25
 */
@Slf4j
@Configuration
public class AdvisorConfiguration {

    /**
     * 创建 MySimpleLoggerAdvisor 对象
     *
     * @return 返回创建好的 MySimpleLoggerAdvisor 对象 {@link MySimpleLoggerAdvisor}
     */
    @Bean
    @ConditionalOnMissingBean
    public MySimpleLoggerAdvisor mySimpleLoggerAdvisor() {
        log.info("Create Customization Advisor Object: MySimpleLoggerAdvisor Object , Used to enhance ChatClient functionality");
        return new MySimpleLoggerAdvisor();
    }
}
