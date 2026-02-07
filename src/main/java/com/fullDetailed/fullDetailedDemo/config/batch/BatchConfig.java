package com.fullDetailed.fullDetailedDemo.config.batch;

import com.fullDetailed.fullDetailedDemo.domain.dtos.Case.CaseCsvDto;
import com.fullDetailed.fullDetailedDemo.domain.entities.Case;
import com.fullDetailed.fullDetailedDemo.repository.CaseRepository;
import com.fullDetailed.fullDetailedDemo.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.EnableJdbcJobRepository;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.Step;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.infrastructure.item.data.RepositoryItemWriter;
import org.springframework.batch.infrastructure.item.file.FlatFileItemReader;
import org.springframework.batch.infrastructure.item.file.LineMapper;
import org.springframework.batch.infrastructure.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.infrastructure.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.infrastructure.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@RequiredArgsConstructor
@EnableBatchProcessing(taskExecutorRef = "batchTaskExecutor")
@EnableJdbcJobRepository(dataSourceRef = "batchDataSource", transactionManagerRef = "batchTransactionManager")
public class BatchConfig {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    private final CaseEntityItemProcessor caseEntityItemProcessor;
    private final CaseRepository caseRepository;

    @Bean
    @StepScope
    public FlatFileItemReader<CaseCsvDto> itemReader(@Value("#{jobParameters['fileName']}") String fileName) {
        FlatFileItemReader<CaseCsvDto> reader = new FlatFileItemReader<>(lineMapper());

        reader.setResource(new FileSystemResource(fileName));
        reader.setLinesToSkip(1);
        reader.setLineMapper(lineMapper());
        return reader;
    }

    @Bean
    public RepositoryItemWriter<Case> itemWriter() {
        RepositoryItemWriter<Case> writer = new RepositoryItemWriter<>(caseRepository);
        writer.setRepository(caseRepository);
        writer.setMethodName("save");
        return writer;
    }

    @Bean
    public Step importStep() {
        return new StepBuilder("csvImport", jobRepository)
                .<CaseCsvDto, Case>chunk(10)
                .reader(itemReader(null))
                .processor(caseEntityItemProcessor)
                .transactionManager(transactionManager)
                .writer(itemWriter())
                .build();
    }

    @Bean
    public Job runJob() {
        return new JobBuilder("importCases", jobRepository)
                .start(importStep())
                .build();
    }

    private LineMapper<CaseCsvDto> lineMapper() {
        DefaultLineMapper<CaseCsvDto> lineMapper = new DefaultLineMapper<>();

        DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
        lineTokenizer.setDelimiter(",");
        lineTokenizer.setStrict(false);
        lineTokenizer.setNames("caseNumber", "title", "description", "status", "judgeId", "lawyerId", "assignedById", "courtRuling");

        BeanWrapperFieldSetMapper<CaseCsvDto> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
        fieldSetMapper.setTargetType(CaseCsvDto.class);

        lineMapper.setLineTokenizer(lineTokenizer);
        lineMapper.setFieldSetMapper(fieldSetMapper);

        return lineMapper;
    }
}
