package com.urutare.stockmuser.dto.company;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CreateBranchBody {
    @Schema(example = "Kigali")
    private String name;

    @Schema(example = "Kigali")
    private String location;

    @Schema(example = "0780000000")
    private String contactInformation;

    public BranchDTO toBranchDTO() {
        BranchDTO branchDTO = new BranchDTO();
        branchDTO.setName(this.name);
        branchDTO.setLocation(this.location);
        branchDTO.setContactInformation(this.contactInformation);
        return branchDTO;
    }
}
