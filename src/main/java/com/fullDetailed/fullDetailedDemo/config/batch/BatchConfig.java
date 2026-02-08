package com.fullDetailed.fullDetailedDemo.config.batch;

import com.fullDetailed.fullDetailedDemo.domain.dtos.Case.CaseCsvDto;
import com.fullDetailed.fullDetailedDemo.domain.entities.Case;
import com.fullDetailed.fullDetailedDemo.repository.CaseRepository;
import com.fullDetailed.fullDetailedDemo.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.Step;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.infrastructure.item.data.RepositoryItemWriter;
import org.springframework.batch.infrastructure.item.data.builder.RepositoryItemWriterBuilder;
import org.springframework.batch.infrastructure.item.file.FlatFileItemReader;
import org.springframework.batch.infrastructure.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@RequiredArgsConstructor
public class BatchConfig {

    private final CaseRepository caseRepository;

    @Bean
    @StepScope
    public FlatFileItemReader<CaseCsvDto> reader(
            @Value("#{jobParameters['filePath']}") String filePath
    ) {
        return new FlatFileItemReaderBuilder<CaseCsvDto>()
                .name("caseItemReader")
                .resource(new FileSystemResource(filePath))
                .linesToSkip(1)  // Skip header row
                .delimited()
                .delimiter(",")
                .names("caseNumber", "title", "description", "status",
                        "judgeId", "lawyerId", "assignedById", "courtRuling")
                .targetType(CaseCsvDto.class)
                .build();
    }

    @Bean
    public CaseEntityItemProcessor processor(UserRepo userRepo) {
        return new CaseEntityItemProcessor(userRepo);
    }

    @Bean
    public RepositoryItemWriter<Case> writer() {
        return new RepositoryItemWriterBuilder<Case>()
                .repository(caseRepository)
                .methodName("save")
                .build();
    }

    @Bean
    public Step importStep(
            JobRepository jobRepository,
            PlatformTransactionManager transactionManager,
            FlatFileItemReader<CaseCsvDto> reader,
            CaseEntityItemProcessor processor,
            RepositoryItemWriter<Case> writer
    ) {
        return new StepBuilder("csvImportStep", jobRepository)
                .<CaseCsvDto, Case>chunk(10)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .transactionManager(transactionManager)
                .build();
    }

    @Bean
    public Job importCaseJob(
            JobRepository jobRepository,
            Step importStep,
            JobCompletionNotificationListener listener
    ) {
        return new JobBuilder("importCaseJob", jobRepository)
                .listener(listener)
                .start(importStep)
                .build();
    }
}