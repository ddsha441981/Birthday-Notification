package com.cwc.birthday.notification.batch;

import com.cwc.birthday.notification.model.Birthday;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@EnableBatchProcessing
public class BatchConfig {

    private JobRepository jobRepository;
    private PlatformTransactionManager transactionManager;

    public BatchConfig(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        this.jobRepository = jobRepository;
        this.transactionManager = transactionManager;
    }

    @Bean
    public Job importBirthdayJob() {
        return new JobBuilder("importBirthdayJob", jobRepository)
                .start(step1())
                .build();
    }

    @Bean
    public Step step1() {
        return new StepBuilder("step1", jobRepository)
                .<Birthday, Birthday>chunk(10, transactionManager)
                .reader(reader())
                .processor(processor())
                .writer(writer())
                .build();
    }

    @Bean
    public ExcelItemReader reader() {
        try {
            return new ExcelItemReader();
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize ExcelItemReader", e);
        }
    }

    @Bean
    public BirthdayItemProcessor processor() {
        return new BirthdayItemProcessor();
    }

    @Bean
    public BirthdayItemWriter writer() {
        return new BirthdayItemWriter();
    }
}
