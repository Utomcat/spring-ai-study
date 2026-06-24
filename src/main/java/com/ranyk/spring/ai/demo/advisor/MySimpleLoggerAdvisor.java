package com.ranyk.spring.ai.demo.advisor;

import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.ai.chat.client.ChatClientRequest;
import org.springframework.ai.chat.client.ChatClientResponse;
import org.springframework.ai.chat.client.advisor.api.CallAdvisor;
import org.springframework.ai.chat.client.advisor.api.CallAdvisorChain;
import org.springframework.ai.chat.client.advisor.api.StreamAdvisor;
import org.springframework.ai.chat.client.advisor.api.StreamAdvisorChain;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

/**
 * CLASS_NAME: MySimpleLoggerAdvisor.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 自定义切面 Advisor 拦截器, 实现 {@link CallAdvisor} (阻塞式请求) 和 {@link StreamAdvisor} (流式请求) 接口
 * @date: 2026-06-23
 */
@Slf4j
@Component
public class MySimpleLoggerAdvisor implements CallAdvisor, StreamAdvisor {


    @Override
    public @NonNull ChatClientResponse adviseCall(@NonNull ChatClientRequest chatClientRequest, @NonNull CallAdvisorChain callAdvisorChain) {
        log.info("MySimpleLoggerAdvisor - adviseCall - Send Request Before: {}", chatClientRequest);
        ChatClientResponse chatClientResponse = callAdvisorChain.nextCall(chatClientRequest);

        if (chatClientResponse.chatResponse() != null && chatClientResponse.chatResponse().getResult() != null) {
            String content = chatClientResponse.chatResponse().getResult().getOutput().getText();
            log.info("MySimpleLoggerAdvisor - adviseCall - Response Content Length: {}", content != null ? content.length() : 0);
            log.info("MySimpleLoggerAdvisor - adviseCall - Response Content: {}", content);
        } else {
            log.warn("MySimpleLoggerAdvisor - adviseCall - Response is null or empty");
        }

        return chatClientResponse;
    }

    @Override
    public @NonNull Flux<ChatClientResponse> adviseStream(@NonNull ChatClientRequest chatClientRequest, @NonNull StreamAdvisorChain streamAdvisorChain) {
        log.info("MySimpleLoggerAdvisor - adviseStream - Send Request Before: {}", chatClientRequest);
        return streamAdvisorChain.nextStream(chatClientRequest).doOnNext(response ->  {
            if (response.chatResponse() != null && response.chatResponse().getResult() != null) {
                String content = response.chatResponse().getResult().getOutput().getText();
                log.info("MySimpleLoggerAdvisor - adviseStream - Response Fragment: {}", content);
            } else {
                log.info("MySimpleLoggerAdvisor - adviseStream - Response fragment is null or empty");
            }
        });
    }

    /**
     * Return the name of the advisor.
     *
     * @return the advisor name.
     */
    @Override
    public @NonNull String getName() {
        return "mySimpleLoggerAdvisor";
    }

    /**
     * Get the order value of this object.
     * <p>Higher values are interpreted as lower priority. As a consequence,
     * the object with the lowest value has the highest priority (somewhat
     * analogous to Servlet {@code load-on-startup} values).
     * <p>Same order values will result in arbitrary sort positions for the
     * affected objects.
     *
     * @return the order value
     * @see #HIGHEST_PRECEDENCE
     * @see #LOWEST_PRECEDENCE
     */
    @Override
    public int getOrder() {
        return 0;
    }
}
