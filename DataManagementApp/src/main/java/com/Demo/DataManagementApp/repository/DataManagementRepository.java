package com.Demo.DataManagementApp.repository;

import com.Demo.DataManagementApp.model.DataManagement;
import com.Demo.DataManagementApp.model.DataManagementKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DataManagementRepository extends JpaRepository<DataManagement, DataManagementKey> {

    List<DataManagement> findByIdFirstNameAndIdLastNameAndOrganizationId(String firstName, String lastName, String organizationId);
    
    
    @Query("SELECT d FROM DataManagement d WHERE " +
            "(d.id.firstName = :firstName AND d.id.lastName = :lastName AND d.organizationId = :organizationId)")
     List<DataManagement> findByBatch(@Param("firstName") List<String> firstNames, 
                                      @Param("lastName") List<String> lastNames,
                                      @Param("organizationId") List<String> organizationIds);
}
