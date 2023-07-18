package com.urutare.stockmuser.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.urutare.stockmuser.entity.Branch;
import com.urutare.stockmuser.entity.Company;
import com.urutare.stockmuser.repository.BranchRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class BranchService {
    private final BranchRepository branchRepository;
    private final CompanyService companyService;

    public Branch addBranch(UUID companyId, Branch branch) {
        Company company = companyService.getCompanyById(companyId);
        if (company != null) {
            branch.setCompany(company);
            return branchRepository.save(branch);
        }
        return null;
    }

    public Branch getBranchById(UUID branchId) {
        return branchRepository.findById(branchId).orElse(null);
    }

    public Branch updateBranch(UUID branchId, Branch updatedBranch) {
        Branch existingBranch = getBranchById(branchId);
        if (existingBranch != null) {
            existingBranch.setName(updatedBranch.getName());
            existingBranch.setLocation(updatedBranch.getLocation());
            existingBranch.setContactInformation(updatedBranch.getContactInformation());
            return branchRepository.save(existingBranch);
        }
        return null;
    }

    public void deleteBranch(UUID branchId) {
        branchRepository.deleteById(branchId);
    }

    public List<Branch> getAllBranchesByCompany(UUID companyId) {
        return branchRepository.findByCompany_Id(companyId);
    }
}
