package com.Demo.DataManagementApp.batch;

import org.springframework.stereotype.Component;

import com.Demo.DataManagementApp.dto.DataResponseDTO;

import java.util.ArrayList;
import java.util.List;

@Component
public class BatchResultHolder {
    private final List<DataResponseDTO> processedData = new ArrayList<>();

    public synchronized void addProcessedData(List<DataResponseDTO> data) {
        processedData.addAll(data);
        System.out.println("BatchResultHolder: Added " + data.size() + " items");
    }

    public synchronized List<DataResponseDTO> getProcessedData() {
        return new ArrayList<>(processedData);
    }

    public synchronized void clearProcessedData() {
        processedData.clear();
    }
    
}

