package com.Demo.DataManagementApp.batch;

import org.springframework.batch.item.ItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.Demo.DataManagementApp.dto.DataRequestDTO;
import com.Demo.DataManagementApp.model.DataManagement;
import com.Demo.DataManagementApp.repository.DataManagementRepository;

import java.util.List;

@Component
public class DataRequestReader implements ItemReader<DataManagement> {

    @Autowired
    private DataManagementRepository repository;

    private List<DataManagement> dataBatch;
    private int index = 0;

    @Override
    public DataManagement read() throws Exception {
        if (dataBatch == null || index >= dataBatch.size()) {
            return null; // No more items to read
        }
        return dataBatch.get(index++);
    }

    public void setBatch(List<DataRequestDTO> requestDTOs) {
        // Extract fields for batch query
        List<String> firstNames = requestDTOs.stream().map(DataRequestDTO::getFirstName).toList();
        List<String> lastNames = requestDTOs.stream().map(DataRequestDTO::getLastName).toList();
        List<String> organizationIds = requestDTOs.stream().map(DataRequestDTO::getOrganizationId).toList();

        // Fetch data in a single batch query
        this.dataBatch = repository.findByBatch(firstNames, lastNames, organizationIds);
        this.index = 0; // Reset index for the new batch
    }
}
