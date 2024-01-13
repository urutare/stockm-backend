package com.urutare.stockmuser.controller;

import com.urutare.stockmuser.dto.company.BranchDTO;
import com.urutare.stockmuser.dto.company.CreateBranchBody;
import com.urutare.stockmuser.entity.Branch;
import com.urutare.stockmuser.entity.Company;
import com.urutare.stockmuser.service.BranchService;
import com.urutare.stockmuser.service.CompanyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/user-service/companies/{companyId}/branches")
@AllArgsConstructor
@Tag(name = "Branch", description = "Branch API")
@SecurityRequirement(name = "bearerAuth")
public class BranchController {
    private final BranchService branchService;
    private final CompanyService companyService;

    @GetMapping
    @Operation(summary = "Get all branches in a company", description = "Get all branches in a company", tags = {
            "Branch"})
    public ResponseEntity<List<BranchDTO>> getAllBranchesByCompany(@PathVariable UUID companyId) {
        List<Branch> branches = branchService.getAllBranchesByCompany(companyId);
        List<BranchDTO> branchDTOs = branches.stream()
                .map(BranchDTO::new)
                .collect(Collectors.toList());

        return ResponseEntity.ok(branchDTOs);
    }

    @PostMapping
    @Operation(summary = "Add a new branch to a company", description = "Add a new branch to a company", tags = {
            "Branch"})
    public ResponseEntity<BranchDTO> addBranch(@PathVariable UUID companyId, @RequestBody CreateBranchBody createBranchBody, @RequestHeader("userId") UUID userId) {
        Company company = companyService.getCompanyById(companyId);
        Branch branch = createBranchBody.toBranchDTO().toEntity();
        branch.setCompany(company);
        branch.setCreatedBy(userId);
        Branch addedBranch = branchService.addBranch(companyId, branch);
        return ResponseEntity.status(HttpStatus.CREATED).body(new BranchDTO(addedBranch));
    }

    @GetMapping("/{branchId}")
    @Operation(summary = "Get a branch by ID", description = "Get a branch by ID", tags = {"Branch"})
    public ResponseEntity<BranchDTO> getBranchById(@PathVariable UUID branchId) {
        Branch branch = branchService.getBranchById(branchId);
        return branch != null ? ResponseEntity.ok(new BranchDTO(branch)) : ResponseEntity.notFound().build();
    }

    @PutMapping("/{branchId}")
    @Operation(summary = "Update a branch", description = "Update a branch", tags = {"Branch"})
    public ResponseEntity<BranchDTO> updateBranch(@PathVariable UUID branchId, @RequestBody CreateBranchBody createBranchBody, @RequestHeader("userId") UUID userId) {
        Branch branch = createBranchBody.toBranchDTO().toEntity();
        branch.setUpdatedBy(userId);
        Branch updatedBranch = branchService.updateBranch(branchId, branch);
        return updatedBranch != null ? ResponseEntity.ok(new BranchDTO(branch)) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{branchId}")
    @Operation(summary = "Delete a branch", description = "Delete a branch", tags = {"Branch"})
    public ResponseEntity<Void> deleteBranch(@PathVariable UUID branchId) {
        branchService.deleteBranch(branchId);
        return ResponseEntity.noContent().build();
    }
}
