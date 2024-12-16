package com.Demo.DataManagementApp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Demo.DataManagementApp.dto.DataRequestDTO;
import com.Demo.DataManagementApp.dto.DataResponseDTO;
import com.Demo.DataManagementApp.model.DataManagement;
import com.Demo.DataManagementApp.model.DataManagementKey;
import com.Demo.DataManagementApp.repository.DataManagementRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DataManagementService {

    @Autowired
    private DataManagementRepository repository;

    // Batch size for processing requests
    private static final int BATCH_SIZE = 50; 
    
 // Save uploaded users into the database
    public void saveUsers(List<DataRequestDTO> requestDTOList) {
        for (int i = 0; i < requestDTOList.size(); i += BATCH_SIZE) {
            int end = Math.min(i + BATCH_SIZE, requestDTOList.size());
            List<DataRequestDTO> chunk = requestDTOList.subList(i, end);
            saveChunk(chunk);
        }
    }

    private void saveChunk(List<DataRequestDTO> chunk) {
        // Step 1: Filter out invalid data (null emails)
        List<DataManagement> validData = chunk.stream()
                .filter(dto -> dto.getEmail() != null) // Exclude entries with null email
                .map(this::mapToEntity)
                .collect(Collectors.toList());

        // Step 2: Retrieve existing records to avoid duplicates
        List<DataManagementKey> keys = validData.stream()
                .map(DataManagement::getId)
                .collect(Collectors.toList());
        List<DataManagementKey> existingKeys = repository.findExistingKeys(keys);

        // Step 3: Filter out duplicates
        List<DataManagement> uniqueData = validData.stream()
                .filter(data -> !existingKeys.contains(data.getId()))
                .collect(Collectors.toList());

        // Step 4: Perform a bulk insert for the remaining unique data
        if (!uniqueData.isEmpty()) {
            repository.saveAll(uniqueData);
            System.out.println("Batch saved: " + uniqueData.size() + " records");
        } else {
            System.out.println("No new records to save in this chunk.");
        }
    }

    // The method to find users
    public List<DataResponseDTO> findUsers(List<DataRequestDTO> requestDTOList) {
        List<DataResponseDTO> responseList = new ArrayList<>();

        // Split requests into smaller batches and process each batch
        for (int i = 0; i < requestDTOList.size(); i += BATCH_SIZE) {
            int end = Math.min(i + BATCH_SIZE, requestDTOList.size());
            List<DataRequestDTO> batch = requestDTOList.subList(i, end);

            // Process each batch of requests
            responseList.addAll(processBatch(batch));
        }

        return responseList;
    }

    // Helper method to process each batch
//    private List<DataResponseDTO> processBatch(List<DataRequestDTO> batch) {
//        List<DataManagement> dataManagementList = new ArrayList<>();
//
//        // Collect data for each request in the batch
//        for (DataRequestDTO requestDTO : batch) {
//            dataManagementList.addAll(repository.findByIdFirstNameAndIdLastNameAndOrganizationId(
//                    requestDTO.getFirstName(),
//                    requestDTO.getLastName(),
//                    requestDTO.getOrganizationId()
//            ));
//        }
//
//        // Convert the result into a list of DataResponseDTOs
//        return dataManagementList.stream()
//                .map(this::mapToResponseDTO)
//                .collect(Collectors.toList());
//    }
    
    private List<DataResponseDTO> processBatch(List<DataRequestDTO> batch) {
        List<String> firstNames = batch.stream().map(DataRequestDTO::getFirstName).collect(Collectors.toList());
        List<String> lastNames = batch.stream().map(DataRequestDTO::getLastName).collect(Collectors.toList());
        List<String> organizationIds = batch.stream().map(DataRequestDTO::getOrganizationId).collect(Collectors.toList());

        List<DataManagement> dataManagementList = repository.findByBatch(firstNames, lastNames, organizationIds);

        return dataManagementList.stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }



    // Method to map DataManagement object to DataResponseDTO
    private DataResponseDTO mapToResponseDTO(DataManagement dataManagement) {
        DataResponseDTO responseDTO = new DataResponseDTO();
        responseDTO.setFirstName(dataManagement.getId().getFirstName());
        responseDTO.setLastName(dataManagement.getId().getLastName());
        responseDTO.setOrganizationId(dataManagement.getOrganizationId());
        responseDTO.setEmail(dataManagement.getEmail());
        return responseDTO;
    }
    
    private DataManagement mapToEntity(DataRequestDTO dto) {
        DataManagement data = new DataManagement();
        DataManagementKey key = new DataManagementKey(dto.getFirstName(), dto.getLastName());
        data.setId(key);
        data.setOrganizationId(dto.getOrganizationId());
        data.setEmail(dto.getEmail());
        return data;
    }
}
