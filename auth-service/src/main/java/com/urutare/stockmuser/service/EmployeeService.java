package com.urutare.stockmuser.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import com.urutare.stockmuser.dto.EmployeeDTO;
import com.urutare.stockmuser.entity.Branch;
import com.urutare.stockmuser.entity.Employee;
import com.urutare.stockmuser.entity.User;
import com.urutare.stockmuser.exception.ResourceNotFoundException;
import com.urutare.stockmuser.repository.BranchRepository;
import com.urutare.stockmuser.repository.EmployeeRepository;
import com.urutare.stockmuser.repository.UserRepository;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    private final BranchRepository branchRepository;

    private final UserRepository userRepository;

    public Employee createEmployee(EmployeeDTO employeeDTO) {
        Branch branch = branchRepository.findById(employeeDTO.getBranchId())
                .orElseThrow(
                        () -> new ResourceNotFoundException("Branch not found with id: " + employeeDTO.getBranchId()));

        User user = userRepository.findById(employeeDTO.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + employeeDTO.getUserId()));

        Employee employee = EmployeeDTO.toEntity(employeeDTO, branch, user);
        return employeeRepository.save(employee);
    }

    public Employee saveEmployee(Employee employee) {
        return employeeRepository.save(employee);
    }

    public Employee getEmployeeById(UUID employeeId) {
        return employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + employeeId));
    }

    public void deleteEmployee(UUID employeeId) {
        Employee employee = getEmployeeById(employeeId);
        employeeRepository.delete(employee);
    }

    public Employee updateEmployee(UUID employeeId, EmployeeDTO employeeDTO) {
        Employee existingEmployee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + employeeId));

        Branch branch = branchRepository.findById(employeeDTO.getBranchId())
                .orElseThrow(
                        () -> new ResourceNotFoundException("Branch not found with id: " + employeeDTO.getBranchId()));

        User user = userRepository.findById(employeeDTO.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + employeeDTO.getUserId()));

        existingEmployee.setName(employeeDTO.getName());
        existingEmployee.setEmail(employeeDTO.getEmail());
        existingEmployee.setContactInformation(employeeDTO.getContactInformation());
        existingEmployee.setPosition(employeeDTO.getPosition());
        existingEmployee.setBranch(branch);
        existingEmployee.setUser(user);

        return employeeRepository.save(existingEmployee);
    }

    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    public List<Employee> searchEmployees(String name, String email, String position) {
        return employeeRepository.searchEmployees(name, email, position);
    }

    public List<Employee> getAllEmployeesByBranch(UUID branchId) {
        return employeeRepository.findByBranchId(branchId);
    }

}
