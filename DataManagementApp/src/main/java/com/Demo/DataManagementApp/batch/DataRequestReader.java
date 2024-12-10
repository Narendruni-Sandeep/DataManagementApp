package com.Demo.DataManagementApp.batch;

import org.springframework.batch.item.ItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.Demo.DataManagementApp.dto.DataRequestDTO;
import com.Demo.DataManagementApp.repository.DataManagementRepository;

import java.util.List;

@Component
public class DataRequestReader implements ItemReader<DataRequestDTO> {

    @Autowired
    private DataManagementRepository repository;

    private List<DataRequestDTO> data;
    private int index = 0;

    @Override
    public DataRequestDTO read() throws Exception {
        if (data == null || index >= data.size()) {
            System.out.println("Reader: No more items to read");
            return null;
        }
        DataRequestDTO currentItem = data.get(index++);
        System.out.println("Reader: Reading item - " + currentItem.getFirstName() + " " + currentItem.getLastName());
        return currentItem;
    }

    public void setData(List<DataRequestDTO> data) {
        this.data = data;
        this.index = 0;
    }
}
