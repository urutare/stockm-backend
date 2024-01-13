package com.urutare.stockmuser.controller;

import com.urutare.stockmuser.dto.company.CompanyDTO;
import com.urutare.stockmuser.dto.company.CreateCompanyBody;
import com.urutare.stockmuser.entity.Company;
import com.urutare.stockmuser.entity.User;
import com.urutare.stockmuser.service.CompanyService;
import com.urutare.stockmuser.service.UserService;
import com.urutare.stockmuser.utils.CloudinaryUtil;
import com.urutare.stockmuser.utils.JwtTokenUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/user-service/companies")
@AllArgsConstructor
@Tag(name = "Company", description = "Company API")
@SecurityRequirement(name = "bearerAuth")
public class CompanyController {
    private final CompanyService companyService;
    private final JwtTokenUtil jwtUtils;
    private final CloudinaryUtil cloudinaryUtil;
    private final UserService userService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Create a new company", description = "Create a new company", tags = {"Company"})
    public ResponseEntity<CompanyDTO> createCompany(HttpServletRequest request, @ModelAttribute CreateCompanyBody createCompanyBody, @RequestHeader("userId") UUID userId) {
        User user = userService.findByID(userId);
        CompanyDTO companyDTO = createCompanyBody.toCompanyDTO();
        Company company = companyDTO.toEntity();
        company.setCreatedBy(userId);
        company.setUser(user);
        if (createCompanyBody.getLogo() != null) {
            try {
                String imageUrl = cloudinaryUtil.uploadImage(createCompanyBody.getLogo());
                company.setLogo(imageUrl);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        Company createdCompany = companyService.createCompany(userId, company);
        CompanyDTO responseDTO = new CompanyDTO(createdCompany);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }

    @GetMapping("/{companyId}")
    @Operation(summary = "Get a company by ID", description = "Get a company by ID", tags = {"Company"})
    public ResponseEntity<CompanyDTO> getCompanyById(@PathVariable UUID companyId) {
        Company company = companyService.getCompanyById(companyId);
        if (company != null && !company.isDeleted()) {
            return ResponseEntity.ok(new CompanyDTO(company));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    @Operation(summary = "Get all companies", description = "Get all companies", tags = {"Company"})
    public ResponseEntity<List<CompanyDTO>> getAllCompanies() {
        List<Company> companies = companyService.getAllCompanies();
        List<CompanyDTO> companyDTOS = companies.stream().map(CompanyDTO::new).toList();
        return ResponseEntity.ok(companyDTOS);
    }

    @PutMapping(path = "/{companyId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Update a company by ID", description = "Update a company by ID", tags = {"Company"})
    public ResponseEntity<Company> updateCompany(@PathVariable UUID companyId, @ModelAttribute CreateCompanyBody companyBody, @RequestHeader("userId") UUID userId) {
        Company existingCompany = companyService.getCompanyById(companyId);

        if (existingCompany != null && !existingCompany.isDeleted()) {
            Company company = companyBody.toCompanyDTO().toEntity();
            company.setCreatedBy(existingCompany.getCreatedBy());
            company.setUpdatedBy(userId);
            if (companyBody.getLogo() != null) {
                try {
                    String imageUrl = cloudinaryUtil.uploadImage(companyBody.getLogo());
                    company.setLogo(imageUrl);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            Company updatedCompany = companyService.saveCompany(company);
            return ResponseEntity.ok(updatedCompany);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{companyId}")
    @Operation(summary = "Delete a company by ID", description = "Delete a company by ID", tags = {"Company"})
    public ResponseEntity<Void> deleteCompany(@PathVariable UUID companyId) {
        companyService.deleteCompany(companyId);
        return ResponseEntity.noContent().build();
    }
}
