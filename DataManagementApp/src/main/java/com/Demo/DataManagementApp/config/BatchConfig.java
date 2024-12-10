package com.Demo.DataManagementApp.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.support.transaction.ResourcelessTransactionManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import com.Demo.DataManagementApp.dto.DataRequestDTO;
import com.Demo.DataManagementApp.dto.DataResponseDTO;

@Configuration
@EnableBatchProcessing
public class BatchConfig {

    @Bean
    public PlatformTransactionManager transactionManager() {
        return new ResourcelessTransactionManager();
    }

    @Bean
    public Job batchJob(JobRepository jobRepository, PlatformTransactionManager transactionManager,
                        ItemReader<DataRequestDTO> reader, ItemProcessor<DataRequestDTO, DataResponseDTO> processor,
                        ItemWriter<DataResponseDTO> writer) {
        Step step = new StepBuilder("step1", jobRepository)
                .<DataRequestDTO, DataResponseDTO>chunk(100, transactionManager)  // Adjust the chunk size here
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .build();

        return new JobBuilder("dataManagementJob", jobRepository)
                .start(step)
                .build();
    }
}
