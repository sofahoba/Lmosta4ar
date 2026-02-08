package com.fullDetailed.fullDetailedDemo.config.batch;

import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.job.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListener;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class JobCompletionNotificationListener implements JobExecutionListener {

    @Override
    public void beforeJob(JobExecution jobExecution) {
        log.info("Job STARTED: {}", jobExecution.getJobInstance().getJobName());
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
            log.info("!!! JOB FINISHED SUCCESSFULLY !!!");
        } else if (jobExecution.getStatus() == BatchStatus.FAILED) {
            log.error("!!! JOB FAILED !!!");
            jobExecution.getAllFailureExceptions()
                    .forEach(e -> log.error("Exception: {}", e.getMessage()));
        }
    }
}