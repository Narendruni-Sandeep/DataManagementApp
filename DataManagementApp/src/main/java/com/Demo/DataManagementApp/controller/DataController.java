package com.Demo.DataManagementApp.controller;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.Demo.DataManagementApp.batch.BatchResultHolder;
import com.Demo.DataManagementApp.batch.DataRequestReader;
import com.Demo.DataManagementApp.dto.DataRequestDTO;
import com.Demo.DataManagementApp.dto.DataResponseDTO;
import com.Demo.DataManagementApp.service.DataManagementService;

import java.util.List;

@RestController
@RequestMapping("/api/data")
public class DataController {

    @Autowired
    private DataManagementService service;

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private Job batchJob;

    @Autowired
    private BatchResultHolder batchResultHolder;

    @Autowired
    private DataRequestReader reader;

    // Endpoint for triggering batch processing (for large dataset processing)
    @PostMapping("/process")
    public ResponseEntity<List<DataResponseDTO>> processBatch(@RequestBody List<DataRequestDTO> requestData) {
        try {
            // Clear previous results from BatchResultHolder
            batchResultHolder.clearProcessedData();

            // Populate the reader with batched data
            reader.setBatch(requestData);

            // Run the batch job
            JobExecution jobExecution = jobLauncher.run(batchJob,
                    new JobParametersBuilder()
                            .addString("jobId", String.valueOf(System.currentTimeMillis()))
                            .toJobParameters());

            // Wait for the job to complete
            while (jobExecution.isRunning()) {
                Thread.sleep(100); // Pause briefly to avoid busy waiting
            }

            // Retrieve processed data
            List<DataResponseDTO> processedData = batchResultHolder.getProcessedData();

            // Return the processed data as a JSON response
            return ResponseEntity.ok(processedData);
        } catch (Exception e) {
            System.err.println("Error occurred during batch processing: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(500).body(null);
        }
    }

    // Endpoint for fetching data using the service with batch processing logic
    @PostMapping("/fetch")
    public ResponseEntity<List<DataResponseDTO>> fetchData(@RequestBody List<DataRequestDTO> requestData) {
        try {
            // Use the service's batch logic for processing and fetching data efficiently
            List<DataResponseDTO> foundData = service.findUsers(requestData);
            return ResponseEntity.ok(foundData);
        } catch (Exception e) {
            System.err.println("Error occurred during data fetch: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(500).body(null);
        }
    }
}
