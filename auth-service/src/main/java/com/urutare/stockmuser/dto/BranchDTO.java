package com.urutare.stockmuser.dto;

import java.util.UUID;

import com.urutare.stockmuser.entity.Branch;

import lombok.Data;

@Data
public class BranchDTO {
    private UUID id;
    private String name;
    private String location;
    private String contactInformation;

    public BranchDTO(Branch branch) {
        this.id = branch.getId();
        this.name = branch.getName();
        this.location = branch.getLocation();
        this.contactInformation = branch.getContactInformation();
    }
}
