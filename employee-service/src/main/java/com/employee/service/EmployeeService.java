package com.employee.service;

import org.springframework.data.domain.Page;

import com.employee.dto.request.EmployeeRequest;
import com.employee.dto.response.EmployeeDepartmentResponse;
import com.employee.dto.response.EmployeeResponse;

public interface EmployeeService {

	EmployeeResponse createEmployee(EmployeeRequest request);

	EmployeeResponse getEmployeeById(Long id);

	EmployeeResponse getEmployeeByEmployeeId(String employeeId);

	Page<EmployeeResponse> getAllEmployees(int page, int size, String sortBy);

	EmployeeResponse updateEmployee(Long id, EmployeeRequest request);

	void deleteEmployee(Long id);

	EmployeeResponse getEmployeeByEmail(String email);
	
    EmployeeDepartmentResponse getEmployeeWithDepartment(Long id);
    
}