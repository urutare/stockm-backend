package com.urutare.stockmuser.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.urutare.stockmuser.entity.Company;
import com.urutare.stockmuser.service.CompanyService;
import com.urutare.stockmuser.utils.JwtTokenUtil;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;

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

    @PostMapping
    @Operation(summary = "Create a new company", description = "Create a new company", tags = { "Company" })
    public ResponseEntity<Company> createCompany(HttpServletRequest request, @RequestBody Company company, @RequestHeader("userId") UUID userId) {
        company.setCreatedBy(userId);
        Company createdCompany = companyService.createCompany(userId, company);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCompany);
    }

    @GetMapping("/{companyId}")
    @Operation(summary = "Get a company by ID", description = "Get a company by ID", tags = { "Company" })
    public ResponseEntity<Company> getCompanyById(@PathVariable UUID companyId) {
        Company company = companyService.getCompanyById(companyId);
        if (company != null && !company.isDeleted()) {
            return ResponseEntity.ok(company);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    @Operation(summary = "Get all companies", description = "Get all companies", tags = { "Company" })
    public ResponseEntity<List<Company>> getAllCompanies() {
        List<Company> companies = companyService.getAllCompanies();
        return ResponseEntity.ok(companies);
    }

    @PutMapping("/{companyId}")
    @Operation(summary = "Update a company by ID", description = "Update a company by ID", tags = { "Company" })
    public ResponseEntity<Company> updateCompany(@PathVariable UUID companyId, @RequestBody Company company, @RequestHeader("userId") UUID userId) {
        Company existingCompany = companyService.getCompanyById(companyId);
        if (existingCompany != null && !existingCompany.isDeleted()) {
            company.setId(existingCompany.getId());
            company.setUpdatedBy(userId);
            Company updatedCompany = companyService.saveCompany(company);
            return ResponseEntity.ok(updatedCompany);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{companyId}")
    @Operation(summary = "Delete a company by ID", description = "Delete a company by ID", tags = { "Company" })
    public ResponseEntity<Void> deleteCompany(@PathVariable UUID companyId) {
        companyService.deleteCompany(companyId);
        return ResponseEntity.noContent().build();
    }
}
