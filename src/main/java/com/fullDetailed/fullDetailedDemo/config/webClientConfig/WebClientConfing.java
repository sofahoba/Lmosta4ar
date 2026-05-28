package com.fullDetailed.fullDetailedDemo.config.webClientConfig;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfing {

  @Value("${aiendpoint}")
  private String aiEndpoint;

  @Bean
  public WebClient webClient() {
      return WebClient.builder()
          .baseUrl(aiEndpoint)
          .build();
  }

}
