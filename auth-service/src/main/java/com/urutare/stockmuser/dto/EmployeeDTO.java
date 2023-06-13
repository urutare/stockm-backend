package com.urutare.stockmuser.dto;

import java.util.UUID;

import com.urutare.stockmuser.entity.Branch;
import com.urutare.stockmuser.entity.Employee;
import com.urutare.stockmuser.entity.User;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class EmployeeDTO {
    private UUID id;
    private String name;
    private String email;
    private String contactInformation;
    private String position;
    private UUID userId;
    private UUID branchId;

    public EmployeeDTO(Employee employee) {
        this.id = employee.getId();
        this.name = employee.getName();
        this.email = employee.getEmail();
        this.contactInformation = employee.getContactInformation();
        this.position = employee.getPosition();
        this.userId = employee.getUser().getId();
        this.branchId = employee.getBranch().getId();
    }

    public static Employee toEntity(EmployeeDTO employeeDTO, Branch branch, User user) {
        Employee employee = new Employee();
        employee.setName(employeeDTO.getName());
        employee.setEmail(employeeDTO.getEmail());
        employee.setContactInformation(employeeDTO.getContactInformation());
        employee.setPosition(employeeDTO.getPosition());
        employee.setBranch(branch);
        employee.setUser(user);
        return employee;
    }
}
