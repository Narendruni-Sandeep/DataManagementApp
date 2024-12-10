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

    // Constructor injection for DataManagementRepository and BatchResultHolder
    public DataResponseWriter(DataManagementRepository dataManagementRepository, BatchResultHolder batchResultHolder) {
        this.dataManagementRepository = dataManagementRepository;
        this.batchResultHolder = batchResultHolder;
    }

    @Override
    public void write(Chunk<? extends DataResponseDTO> items) throws Exception {
        List<? extends DataResponseDTO> dataResponseDTOList = items.getItems();

        if (dataResponseDTOList.isEmpty()) {
            System.out.println("Writer: No items to write");
        } else {
            System.out.println("Writer: Writing " + dataResponseDTOList.size() + " items");
        }

        // Add processed data to BatchResultHolder
        batchResultHolder.addProcessedData((List<DataResponseDTO>) dataResponseDTOList);

        // Save data to the database
        for (DataResponseDTO item : dataResponseDTOList) {
            DataManagement dataManagement = new DataManagement();
            DataManagementKey key = new DataManagementKey();
            key.setFirstName(item.getFirstName());
            key.setLastName(item.getLastName());

            dataManagement.setId(key);
            dataManagement.setOrganizationId(item.getOrganizationId());
            dataManagement.setEmail(item.getEmail());

            dataManagementRepository.save(dataManagement);
        }

        dataResponseDTOList.forEach(item -> System.out.println("Writer: Saved item - " + item.getEmail()));
    }
}
