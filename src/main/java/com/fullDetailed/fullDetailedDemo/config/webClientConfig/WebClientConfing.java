package com.fullDetailed.fullDetailedDemo.config.webClientConfig;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfing {

  @Bean
  public WebClient webClient(){
    return WebClient.builder()
                    .baseUrl("http://host.docker.internal:8000")
                    .build();
  }

}
