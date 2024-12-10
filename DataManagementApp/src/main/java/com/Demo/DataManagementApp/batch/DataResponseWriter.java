package com.Demo.DataManagementApp.batch;

import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import com.Demo.DataManagementApp.dto.DataResponseDTO;
import com.Demo.DataManagementApp.model.DataManagement;
import com.Demo.DataManagementApp.model.DataManagementKey;
import com.Demo.DataManagementApp.repository.DataManagementRepository;

import java.util.List;

@Component
public class DataResponseWriter implements ItemWriter<DataResponseDTO> {

    private final DataManagementRepository dataManagementRepository;
    private final BatchResultHolder batchResultHolder;

    // Constructor for dependency injection
    public DataResponseWriter(DataManagementRepository dataManagementRepository, BatchResultHolder batchResultHolder) {
        this.dataManagementRepository = dataManagementRepository;
        this.batchResultHolder = batchResultHolder;
    }

    @Override
    public void write(Chunk<? extends DataResponseDTO> items) throws Exception {
        List<? extends DataResponseDTO> dataResponseDTOList = items.getItems();

        if (dataResponseDTOList.isEmpty()) {
            System.out.println("Writer: No items to write");
            return;
        }

        System.out.println("Writer: Writing " + dataResponseDTOList.size() + " items");

        // Add processed data to BatchResultHolder for returning as a JSON response
        batchResultHolder.addProcessedData((List<DataResponseDTO>) dataResponseDTOList);

        // Convert DataResponseDTOs to DataManagement entities and save to the database
        List<DataManagement> dataManagementEntities = dataResponseDTOList.stream()
                .map(this::mapToEntity)
                .toList();

        dataManagementRepository.saveAll(dataManagementEntities);
        System.out.println("Writer: Successfully saved " + dataManagementEntities.size() + " items to the database");
    }

    private DataManagement mapToEntity(DataResponseDTO dto) {
        DataManagement dataManagement = new DataManagement();
        DataManagementKey key = new DataManagementKey();
        key.setFirstName(dto.getFirstName());
        key.setLastName(dto.getLastName());

        dataManagement.setId(key);
        dataManagement.setOrganizationId(dto.getOrganizationId());
        dataManagement.setEmail(dto.getEmail());

        return dataManagement;
    }
}
