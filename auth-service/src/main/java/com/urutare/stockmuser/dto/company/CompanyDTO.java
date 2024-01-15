package com.urutare.stockmuser.dto.company;

import com.urutare.stockmuser.entity.Company;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;


@Data
@NoArgsConstructor
public class CompanyDTO {
    private UUID id;

    private String name;

    private String address;

    private String email;

    private String logo;

    private String tin;

    private String website;

    private String phone;


    public CompanyDTO(Company company) {
        this.id = company.getId();
        this.name = company.getName();
        this.address = company.getAddress();
        this.email = company.getEmail();
        this.logo = company.getLogo();
        this.tin = company.getTin();
        this.website = company.getWebsite();
        this.phone = company.getPhone();
    }

    public Company toEntity() {
        Company company = new Company();
        company.setId(this.id);
        company.setName(this.name);
        company.setAddress(this.address);
        company.setEmail(this.email);
        company.setLogo(this.logo);
        company.setTin(this.tin);
        company.setWebsite(this.website);
        company.setPhone(this.phone);
        return company;
    }

}
