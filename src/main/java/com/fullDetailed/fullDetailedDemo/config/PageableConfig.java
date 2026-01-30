package com.fullDetailed.fullDetailedDemo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.config.PageableHandlerMethodArgumentResolverCustomizer;

@Configuration
public class PageableConfig {

    @Bean
    public PageableHandlerMethodArgumentResolverCustomizer pageableCustomizer() {
        return pageableResolver -> {
            pageableResolver.setFallbackPageable(
                    PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "createdAt"))
            );
            pageableResolver.setMaxPageSize(50);
        };
    }
}
