package com.Demo.DataManagementApp.model;

import jakarta.persistence.*;

@Entity
@Table(name = "demo_data")
public class DataManagement {

    @EmbeddedId
    private DataManagementKey id;

    @Column(name = "organization_domain")
    private String organizationId;

    @Column(name = "email")
    private String email;

    public DataManagementKey getId() {
        return id;
    }

    public void setId(DataManagementKey id) {
        this.id = id;
    }

    public String getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(String organizationId) {
        this.organizationId = organizationId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
