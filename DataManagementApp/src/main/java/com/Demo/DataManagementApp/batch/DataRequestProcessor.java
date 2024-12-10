package com.Demo.DataManagementApp.batch;

import java.util.List;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.Demo.DataManagementApp.dto.DataRequestDTO;
import com.Demo.DataManagementApp.dto.DataResponseDTO;
import com.Demo.DataManagementApp.model.DataManagement;
import com.Demo.DataManagementApp.repository.DataManagementRepository;

@Component
public class DataRequestProcessor implements ItemProcessor<DataRequestDTO, DataResponseDTO> {

    @Autowired
    private DataManagementRepository repository;

    @Override
    public DataResponseDTO process(DataRequestDTO item) throws Exception {
        if (item == null) {
            return null;
        }

        // Fetch email from the database based on the request
        List<DataManagement> results = repository.findByIdFirstNameAndIdLastNameAndOrganizationId(
                item.getFirstName(),
                item.getLastName(),
                item.getOrganizationId()
        );

        if (results.isEmpty()) {
            System.out.println("Processor: No matching records found for " + item);
            return null;
        }

        DataManagement dataManagement = results.get(0); // Assume one record per request
        DataResponseDTO responseDTO = new DataResponseDTO();
        responseDTO.setFirstName(dataManagement.getId().getFirstName());
        responseDTO.setLastName(dataManagement.getId().getLastName());
        responseDTO.setOrganizationId(dataManagement.getOrganizationId());
        responseDTO.setEmail(dataManagement.getEmail());

        System.out.println("Processor: Processed item - " + responseDTO);
        return responseDTO;
    }
}
