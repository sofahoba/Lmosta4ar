package com.fullDetailed.fullDetailedDemo.config.pageable;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.config.PageableHandlerMethodArgumentResolverCustomizer;

@Configuration
public class PageableConfig {

    @Bean
    public PageableHandlerMethodArgumentResolverCustomizer customize() {
        return pageableResolver -> {
            pageableResolver.setFallbackPageable(
                    org.springframework.data.domain.PageRequest.of(
                            0,
                            10,
                            Sort.by(Sort.Direction.DESC, "createdAt")
                    )
            );
        };
    }
}