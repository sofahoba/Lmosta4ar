package com.fullDetailed.fullDetailedDemo;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableBatchProcessing
public class Elmostashar {

	public static void main(String[] args) {
		SpringApplication.run(Elmostashar.class, args);
	}

}