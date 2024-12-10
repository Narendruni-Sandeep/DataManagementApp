package com.Demo.DataManagementApp.batch;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import com.Demo.DataManagementApp.dto.DataRequestDTO;
import com.Demo.DataManagementApp.dto.DataResponseDTO;
import com.Demo.DataManagementApp.model.DataManagement;

@Component
public class DataRequestProcessor implements ItemProcessor<DataManagement, DataResponseDTO> {

    @Override
    public DataResponseDTO process(DataManagement dataManagement) throws Exception {
        if (dataManagement == null) {
            return null;
        }

        // Transform DataManagement into DataResponseDTO
        DataResponseDTO responseDTO = new DataResponseDTO();
        responseDTO.setFirstName(dataManagement.getId().getFirstName());
        responseDTO.setLastName(dataManagement.getId().getLastName());
        responseDTO.setOrganizationId(dataManagement.getOrganizationId());
        responseDTO.setEmail(dataManagement.getEmail());

        // Log the processed result
        System.out.println("Processor: Processed item - " + responseDTO);
        return responseDTO;
    }
}
