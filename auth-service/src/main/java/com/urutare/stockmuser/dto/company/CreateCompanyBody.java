package com.urutare.stockmuser.dto.company;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor
public class CreateCompanyBody {
    @Schema(example = "Urutare")
    private String name;

    @Schema(example = "Kigali, Rwanda")
    private String address;

    @Schema(example = "info@urutare.com")
    private String email;

    @Schema(description = "Company Logo")
    private MultipartFile logo;

    @Schema(example = "123456789")
    private String tin;

    @Schema(example = "www.urutare.com")
    private String website;

    @Schema(example = "250788000000")
    private String phone;

    public CompanyDTO toCompanyDTO() {
        CompanyDTO companyDTO = new CompanyDTO();
        companyDTO.setName(this.name);
        companyDTO.setAddress(this.address);
        companyDTO.setEmail(this.email);
        companyDTO.setTin(this.tin);
        companyDTO.setWebsite(this.website);
        companyDTO.setPhone(this.phone);
        return companyDTO;
    }
}
