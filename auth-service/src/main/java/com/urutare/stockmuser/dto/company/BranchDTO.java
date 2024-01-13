package com.urutare.stockmuser.dto.company;

import com.urutare.stockmuser.entity.Branch;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
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

    public Branch toEntity() {
        Branch branch = new Branch();
        branch.setId(this.id);
        branch.setName(this.name);
        branch.setLocation(this.location);
        branch.setContactInformation(this.contactInformation);
        return branch;
    }
}
