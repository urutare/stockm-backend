package com.urutare.stockmuser.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.urutare.stockmuser.dto.BranchDTO;
import com.urutare.stockmuser.entity.Branch;
import com.urutare.stockmuser.service.BranchService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

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

    @GetMapping
    @Operation(summary = "Get all branches in a company", description = "Get all branches in a company", tags = {
            "Branch" })
    public ResponseEntity<List<BranchDTO>> getAllBranchesByCompany(@PathVariable UUID companyId) {
        List<Branch> branches = branchService.getAllBranchesByCompany(companyId);
        List<BranchDTO> branchDTOs = branches.stream()
                .map(BranchDTO::new)
                .collect(Collectors.toList());

        return ResponseEntity.ok(branchDTOs);
    }

    @PostMapping
    @Operation(summary = "Add a new branch to a company", description = "Add a new branch to a company", tags = {
            "Branch" })
    public ResponseEntity<Branch> addBranch(@PathVariable UUID companyId, @RequestBody Branch branch, @RequestHeader("userId") UUID userId) {
        branch.setCreatedBy(userId);
        Branch addedBranch = branchService.addBranch(companyId, branch);
        return ResponseEntity.status(HttpStatus.CREATED).body(addedBranch);
    }

    @GetMapping("/{branchId}")
    @Operation(summary = "Get a branch by ID", description = "Get a branch by ID", tags = { "Branch" })
    public ResponseEntity<Branch> getBranchById(@PathVariable UUID branchId) {
        Branch branch = branchService.getBranchById(branchId);
        return branch != null ? ResponseEntity.ok(branch) : ResponseEntity.notFound().build();
    }

    @PutMapping("/{branchId}")
    @Operation(summary = "Update a branch", description = "Update a branch", tags = { "Branch" })
    public ResponseEntity<Branch> updateBranch(@PathVariable UUID branchId, @RequestBody Branch updatedBranch, @RequestHeader("userId") UUID userId) {
        updatedBranch.setUpdatedBy(userId);
        Branch branch = branchService.updateBranch(branchId, updatedBranch);
        return branch != null ? ResponseEntity.ok(branch) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{branchId}")
    @Operation(summary = "Delete a branch", description = "Delete a branch", tags = { "Branch" })
    public ResponseEntity<Void> deleteBranch(@PathVariable UUID branchId) {
        branchService.deleteBranch(branchId);
        return ResponseEntity.noContent().build();
    }
}
