package com.example.employee_management.service;

import com.example.employee_management.dto.EmployeeRequestDto;
import com.example.employee_management.dto.EmployeeResponseDto;

import java.util.List;

public interface EmployeeService {
    EmployeeResponseDto createEmployee(EmployeeRequestDto requestDto);
    EmployeeResponseDto getEmployeeById(Long id);
    List<EmployeeResponseDto> getAllEmployees();
    EmployeeResponseDto updateEmployee(Long id, EmployeeRequestDto requestDto);
    void deleteEmployee(Long id);
    List<EmployeeResponseDto> getEmployeesByDepartment(String department);
    List<EmployeeResponseDto> getEmployeesBySalaryRange(Double min, Double max);
    List<EmployeeResponseDto> searchEmployeesByName(String name);
}