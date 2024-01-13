package com.urutare.stockmuser.service;

import com.urutare.stockmuser.entity.Company;
import com.urutare.stockmuser.entity.User;
import com.urutare.stockmuser.repository.CompanyRepository;
import com.urutare.stockmuser.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CompanyService {
    private final CompanyRepository companyRepository;
    private final UserRepository userRepository;

    public Company createCompany(UUID userId, Company company) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            company.setUser(user);
            return companyRepository.save(company);
        }
        throw new IllegalArgumentException("User not found with ID: " + userId);
    }

    public Company saveCompany(Company company) {
        return companyRepository.save(company);
    }

    public Company getCompanyById(UUID companyId) {
        return companyRepository.findByIdAndDeletedFalse(companyId);
    }

    public List<Company> getAllCompanies() {
        return companyRepository.findAllByDeletedFalse();
    }

    public Company updateCompany(UUID companyId, Company updatedCompany) {
        Company existingCompany = getCompanyById(companyId);
        if (existingCompany != null) {
            existingCompany.setName(updatedCompany.getName());
            existingCompany.setAddress(updatedCompany.getAddress());
            existingCompany.setUpdatedBy(updatedCompany.getUpdatedBy());
            existingCompany.setLogo(updatedCompany.getLogo());
            existingCompany.setEmail(updatedCompany.getEmail());
            existingCompany.setPhone(updatedCompany.getPhone());
            existingCompany.setWebsite(updatedCompany.getWebsite());

            return companyRepository.save(existingCompany);
        }
        return null;
    }

    public void deleteCompany(UUID companyId) {
        Company existingCompany = getCompanyById(companyId);
        if (existingCompany != null) {
            existingCompany.setDeleted(true);
            companyRepository.save(existingCompany);
        }
    }
}
