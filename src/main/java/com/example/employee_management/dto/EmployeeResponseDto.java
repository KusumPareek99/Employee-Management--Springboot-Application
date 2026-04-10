package com.example.employee_management.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class EmployeeResponseDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String department;
    private Double salary;
    private LocalDate joiningDate;
    private String fullName; // computed field
}
