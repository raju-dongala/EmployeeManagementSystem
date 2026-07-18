package com.employee.service.Impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.employee.dto.request.EmployeeRequest;
import com.employee.dto.response.DepartmentResponse;
import com.employee.dto.response.EmployeeDepartmentResponse;
import com.employee.dto.response.EmployeeResponse;
import com.employee.entity.Employee;
import com.employee.exception.DuplicateResourceException;
import com.employee.exception.EmployeeNotFoundException;
import com.employee.feign.DepartmentClient;
import com.employee.mapper.EmployeeMapper;
import com.employee.repository.EmployeeRepository;
import com.employee.service.EmployeeService;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

	private final EmployeeRepository employeeRepository;
	private final EmployeeMapper employeeMapper;
	private final DepartmentClient departmentClient;

	@Override
	public EmployeeResponse createEmployee(EmployeeRequest request) {

		if (employeeRepository.existsByEmployeeId(request.getEmployeeId())) {
			throw new DuplicateResourceException("Employee ID already exists");
		}

		if (employeeRepository.existsByEmail(request.getEmail())) {
			throw new DuplicateResourceException("Email already exists");
		}

		Employee employee = employeeMapper.toEntity(request);

		Employee savedEmployee = employeeRepository.save(employee);

		return employeeMapper.toResponse(savedEmployee);
	}

	@Override
	public EmployeeResponse getEmployeeById(Long id) {

		Employee employee = employeeRepository.findById(id)
				.orElseThrow(() -> new EmployeeNotFoundException("Employee not found with ID : " + id));

		return employeeMapper.toResponse(employee);
	}

	@Override
	public EmployeeResponse getEmployeeByEmployeeId(String employeeId) {

		Employee employee = employeeRepository.findByEmployeeId(employeeId).orElseThrow(
				() -> new EmployeeNotFoundException("Employee not found with Employee ID : " + employeeId));

		return employeeMapper.toResponse(employee);
	}

	@Override
	public Page<EmployeeResponse> getAllEmployees(int page, int size, String sortBy) {

		Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));

		return employeeRepository.findAll(pageable).map(employeeMapper::toResponse);
	}

	@Override
	public EmployeeResponse updateEmployee(Long id, EmployeeRequest request) {

		Employee employee = employeeRepository.findById(id)
				.orElseThrow(() -> new EmployeeNotFoundException("Employee not found with ID : " + id));

		if (!employee.getEmail().equals(request.getEmail()) && employeeRepository.existsByEmail(request.getEmail())) {

			throw new DuplicateResourceException("Email already exists");
		}

		if (!employee.getEmployeeId().equals(request.getEmployeeId())
				&& employeeRepository.existsByEmployeeId(request.getEmployeeId())) {

			throw new DuplicateResourceException("Employee ID already exists");
		}

		employeeMapper.updateEntity(employee, request);

		Employee updatedEmployee = employeeRepository.save(employee);

		return employeeMapper.toResponse(updatedEmployee);
	}

	@Override
	public void deleteEmployee(Long id) {

		Employee employee = employeeRepository.findById(id)
				.orElseThrow(() -> new EmployeeNotFoundException("Employee not found with ID : " + id));

		employeeRepository.delete(employee);
	}

	@Override
	public EmployeeResponse getEmployeeByEmail(String email) {

		Employee employee = employeeRepository.findByEmail(email)
				.orElseThrow(() -> new EmployeeNotFoundException("Employee not found with Email : " + email));

		return employeeMapper.toResponse(employee);
	}
	
	@Override
	@CircuitBreaker(
	        name = "department-service",
	        fallbackMethod = "getEmployeeWithDepartmentFallback"
	)
	public EmployeeDepartmentResponse getEmployeeWithDepartment(Long id) {

	    Employee employee = employeeRepository.findById(id)
	            .orElseThrow(() ->
	                    new EmployeeNotFoundException(
	                            "Employee not found with ID : " + id));

	    DepartmentResponse department =
	            departmentClient.getDepartmentByCode(
	                    employee.getDepartmentCode());

	    return EmployeeDepartmentResponse.builder()
	            .employee(employeeMapper.toResponse(employee))
	            .department(department)
	            .build();
	}
	
	
	public EmployeeDepartmentResponse getEmployeeWithDepartmentFallback(
	        Long id,
	        Exception exception) {

	    Employee employee = employeeRepository.findById(id)
	            .orElseThrow(() ->
	                    new EmployeeNotFoundException(
	                            "Employee not found with ID : " + id));

	    DepartmentResponse department = DepartmentResponse.builder()
	            .departmentCode("N/A")
	            .departmentName("Department Service Unavailable")
	            .location("N/A")
	            .headOfDepartment("N/A")
	            .status("UNAVAILABLE")
	            .build();

	    return EmployeeDepartmentResponse.builder()
	            .employee(employeeMapper.toResponse(employee))
	            .department(department)
	            .build();
	}
}