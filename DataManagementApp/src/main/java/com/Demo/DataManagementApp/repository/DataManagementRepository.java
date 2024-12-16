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
            "(d.id.firstName IN :firstNames AND d.id.lastName IN :lastNames AND d.organizationId IN :organizationIds)")
    List<DataManagement> findByBatch(@Param("firstNames") List<String> firstNames, 
                                     @Param("lastNames") List<String> lastNames,
                                     @Param("organizationIds") List<String> organizationIds);
    
    @Query("SELECT d.id FROM DataManagement d WHERE d.id IN :keys")
    List<DataManagementKey> findExistingKeys(@Param("keys") List<DataManagementKey> keys);
}

