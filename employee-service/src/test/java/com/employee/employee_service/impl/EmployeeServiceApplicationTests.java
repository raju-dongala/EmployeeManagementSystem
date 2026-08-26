package com.employee.employee_service.impl;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
import com.employee.service.Impl.EmployeeServiceImpl;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceImplTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private EmployeeMapper employeeMapper;

    @Mock
    private DepartmentClient departmentClient;

    @InjectMocks
    private EmployeeServiceImpl employeeService;

    private Employee employee;
    private EmployeeRequest employeeRequest;
    private EmployeeResponse employeeResponse;

    @BeforeEach
    void setUp() {

        employee = new Employee();
        employee.setId(1L);
        employee.setEmployeeId("EMP001");
        employee.setEmail("employee@test.com");
        employee.setDepartmentCode("IT");

        employeeRequest = new EmployeeRequest();
        employeeRequest.setEmployeeId("EMP001");
        employeeRequest.setEmail("employee@test.com");

        employeeResponse = new EmployeeResponse();
    }

    // -------------------------------------------------------
    // createEmployee()
    // -------------------------------------------------------

    @Test
    void createEmployee_shouldCreateEmployeeSuccessfully() {

        when(employeeRepository.existsByEmployeeId("EMP001"))
                .thenReturn(false);

        when(employeeRepository.existsByEmail("employee@test.com"))
                .thenReturn(false);

        when(employeeMapper.toEntity(employeeRequest))
                .thenReturn(employee);

        when(employeeRepository.save(employee))
                .thenReturn(employee);

        when(employeeMapper.toResponse(employee))
                .thenReturn(employeeResponse);

        EmployeeResponse result =
                employeeService.createEmployee(employeeRequest);

        assertThat(result).isSameAs(employeeResponse);

        verify(employeeRepository)
                .existsByEmployeeId("EMP001");

        verify(employeeRepository)
                .existsByEmail("employee@test.com");

        verify(employeeRepository)
                .save(employee);

        verify(employeeMapper)
                .toResponse(employee);
    }

    @Test
    void createEmployee_shouldThrowExceptionWhenEmployeeIdAlreadyExists() {

        when(employeeRepository.existsByEmployeeId("EMP001"))
                .thenReturn(true);

        assertThatThrownBy(() ->
                employeeService.createEmployee(employeeRequest))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessage("Employee ID already exists");

        verify(employeeRepository)
                .existsByEmployeeId("EMP001");

        verify(employeeRepository, never())
                .save(any(Employee.class));
    }

    @Test
    void createEmployee_shouldThrowExceptionWhenEmailAlreadyExists() {

        when(employeeRepository.existsByEmployeeId("EMP001"))
                .thenReturn(false);

        when(employeeRepository.existsByEmail("employee@test.com"))
                .thenReturn(true);

        assertThatThrownBy(() ->
                employeeService.createEmployee(employeeRequest))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessage("Email already exists");

        verify(employeeRepository)
                .existsByEmail("employee@test.com");

        verify(employeeRepository, never())
                .save(any(Employee.class));
    }

    // -------------------------------------------------------
    // getEmployeeById()
    // -------------------------------------------------------

    @Test
    void getEmployeeById_shouldReturnEmployeeSuccessfully() {

        when(employeeRepository.findById(1L))
                .thenReturn(Optional.of(employee));

        when(employeeMapper.toResponse(employee))
                .thenReturn(employeeResponse);

        EmployeeResponse result =
                employeeService.getEmployeeById(1L);

        assertThat(result).isSameAs(employeeResponse);

        verify(employeeRepository).findById(1L);
        verify(employeeMapper).toResponse(employee);
    }

    @Test
    void getEmployeeById_shouldThrowExceptionWhenEmployeeNotFound() {

        when(employeeRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() ->
                employeeService.getEmployeeById(1L))
                .isInstanceOf(EmployeeNotFoundException.class)
                .hasMessage("Employee not found with ID : 1");

        verify(employeeRepository).findById(1L);

        verify(employeeMapper, never())
                .toResponse(any(Employee.class));
    }

    // -------------------------------------------------------
    // getEmployeeByEmployeeId()
    // -------------------------------------------------------

    @Test
    void getEmployeeByEmployeeId_shouldReturnEmployeeSuccessfully() {

        when(employeeRepository.findByEmployeeId("EMP001"))
                .thenReturn(Optional.of(employee));

        when(employeeMapper.toResponse(employee))
                .thenReturn(employeeResponse);

        EmployeeResponse result =
                employeeService.getEmployeeByEmployeeId("EMP001");

        assertThat(result).isSameAs(employeeResponse);

        verify(employeeRepository)
                .findByEmployeeId("EMP001");

        verify(employeeMapper)
                .toResponse(employee);
    }

    @Test
    void getEmployeeByEmployeeId_shouldThrowExceptionWhenNotFound() {

        when(employeeRepository.findByEmployeeId("EMP001"))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() ->
                employeeService.getEmployeeByEmployeeId("EMP001"))
                .isInstanceOf(EmployeeNotFoundException.class)
                .hasMessage(
                    "Employee not found with Employee ID : EMP001"
                );

        verify(employeeRepository)
                .findByEmployeeId("EMP001");
    }

    // -------------------------------------------------------
    // updateEmployee()
    // -------------------------------------------------------

    @Test
    void updateEmployee_shouldUpdateSuccessfully() {

        EmployeeRequest updateRequest = new EmployeeRequest();
        updateRequest.setEmployeeId("EMP002");
        updateRequest.setEmail("new@test.com");

        when(employeeRepository.findById(1L))
                .thenReturn(Optional.of(employee));

        when(employeeRepository.existsByEmail("new@test.com"))
                .thenReturn(false);

        when(employeeRepository.existsByEmployeeId("EMP002"))
                .thenReturn(false);

        doNothing()
                .when(employeeMapper)
                .updateEntity(employee, updateRequest);

        when(employeeRepository.save(employee))
                .thenReturn(employee);

        when(employeeMapper.toResponse(employee))
                .thenReturn(employeeResponse);

        EmployeeResponse result =
                employeeService.updateEmployee(1L, updateRequest);

        assertThat(result).isSameAs(employeeResponse);

        verify(employeeRepository)
                .findById(1L);

        verify(employeeMapper)
                .updateEntity(employee, updateRequest);

        verify(employeeRepository)
                .save(employee);

        verify(employeeMapper)
                .toResponse(employee);
    }

    @Test
    void updateEmployee_shouldThrowExceptionWhenEmployeeNotFound() {

        when(employeeRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() ->
                employeeService.updateEmployee(1L, employeeRequest))
                .isInstanceOf(EmployeeNotFoundException.class)
                .hasMessage("Employee not found with ID : 1");

        verify(employeeRepository)
                .findById(1L);

        verify(employeeRepository, never())
                .save(any(Employee.class));
    }

    @Test
    void updateEmployee_shouldThrowExceptionWhenEmailAlreadyExists() {

        EmployeeRequest updateRequest = new EmployeeRequest();
        updateRequest.setEmployeeId("EMP001");
        updateRequest.setEmail("another@test.com");

        when(employeeRepository.findById(1L))
                .thenReturn(Optional.of(employee));

        when(employeeRepository.existsByEmail("another@test.com"))
                .thenReturn(true);

        assertThatThrownBy(() ->
                employeeService.updateEmployee(1L, updateRequest))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessage("Email already exists");

        verify(employeeRepository)
                .existsByEmail("another@test.com");

        verify(employeeRepository, never())
                .save(any(Employee.class));
    }

    @Test
    void updateEmployee_shouldThrowExceptionWhenEmployeeIdAlreadyExists() {

        EmployeeRequest updateRequest = new EmployeeRequest();
        updateRequest.setEmployeeId("EMP002");
        updateRequest.setEmail("employee@test.com");

        when(employeeRepository.findById(1L))
                .thenReturn(Optional.of(employee));

        when(employeeRepository.existsByEmployeeId("EMP002"))
                .thenReturn(true);

        assertThatThrownBy(() ->
                employeeService.updateEmployee(1L, updateRequest))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessage("Employee ID already exists");

        verify(employeeRepository)
                .existsByEmployeeId("EMP002");

        verify(employeeRepository, never())
                .save(any(Employee.class));
    }

    // -------------------------------------------------------
    // deleteEmployee()
    // -------------------------------------------------------

    @Test
    void deleteEmployee_shouldDeleteSuccessfully() {

        when(employeeRepository.findById(1L))
                .thenReturn(Optional.of(employee));

        employeeService.deleteEmployee(1L);

        verify(employeeRepository)
                .findById(1L);

        verify(employeeRepository)
                .delete(employee);
    }

    @Test
    void deleteEmployee_shouldThrowExceptionWhenNotFound() {

        when(employeeRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() ->
                employeeService.deleteEmployee(1L))
                .isInstanceOf(EmployeeNotFoundException.class)
                .hasMessage("Employee not found with ID : 1");

        verify(employeeRepository, never())
                .delete(any(Employee.class));
    }

    // -------------------------------------------------------
    // getEmployeeByEmail()
    // -------------------------------------------------------

    @Test
    void getEmployeeByEmail_shouldReturnEmployeeSuccessfully() {

        when(employeeRepository.findByEmail("employee@test.com"))
                .thenReturn(Optional.of(employee));

        when(employeeMapper.toResponse(employee))
                .thenReturn(employeeResponse);

        EmployeeResponse result =
                employeeService.getEmployeeByEmail(
                        "employee@test.com");

        assertThat(result).isSameAs(employeeResponse);

        verify(employeeRepository)
                .findByEmail("employee@test.com");

        verify(employeeMapper)
                .toResponse(employee);
    }

    @Test
    void getEmployeeByEmail_shouldThrowExceptionWhenNotFound() {

        when(employeeRepository.findByEmail("employee@test.com"))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() ->
                employeeService.getEmployeeByEmail(
                        "employee@test.com"))
                .isInstanceOf(EmployeeNotFoundException.class)
                .hasMessage(
                    "Employee not found with Email : employee@test.com"
                );

        verify(employeeRepository)
                .findByEmail("employee@test.com");
    }

    // -------------------------------------------------------
    // getEmployeeWithDepartment()
    // -------------------------------------------------------

    @Test
    void getEmployeeWithDepartment_shouldReturnEmployeeAndDepartment() {

        DepartmentResponse departmentResponse =
                DepartmentResponse.builder()
                        .departmentCode("IT")
                        .departmentName("Information Technology")
                        .location("Hyderabad")
                        .headOfDepartment("John")
                        .status("ACTIVE")
                        .build();

        when(employeeRepository.findById(1L))
                .thenReturn(Optional.of(employee));

        when(departmentClient.getDepartmentByCode("IT"))
                .thenReturn(departmentResponse);

        when(employeeMapper.toResponse(employee))
                .thenReturn(employeeResponse);

        EmployeeDepartmentResponse result =
                employeeService.getEmployeeWithDepartment(1L);

        assertThat(result).isNotNull();
        assertThat(result.getEmployee())
                .isSameAs(employeeResponse);

        assertThat(result.getDepartment())
                .isSameAs(departmentResponse);

        verify(employeeRepository)
                .findById(1L);

        verify(departmentClient)
                .getDepartmentByCode("IT");

        verify(employeeMapper)
                .toResponse(employee);
    }

    @Test
    void getEmployeeWithDepartment_shouldThrowExceptionWhenEmployeeNotFound() {

        when(employeeRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() ->
                employeeService.getEmployeeWithDepartment(1L))
                .isInstanceOf(EmployeeNotFoundException.class)
                .hasMessage("Employee not found with ID : 1");

        verify(employeeRepository)
                .findById(1L);

        verify(departmentClient, never())
                .getDepartmentByCode(anyString());
    }

    // -------------------------------------------------------
    // Circuit breaker fallback
    // -------------------------------------------------------

    @Test
    void getEmployeeWithDepartmentFallback_shouldReturnUnavailableDepartment() {

        when(employeeRepository.findById(1L))
                .thenReturn(Optional.of(employee));

        when(employeeMapper.toResponse(employee))
                .thenReturn(employeeResponse);

        EmployeeDepartmentResponse result =
                employeeService.getEmployeeWithDepartmentFallback(
                        1L,
                        new RuntimeException("Department service down"));

        assertThat(result).isNotNull();

        assertThat(result.getEmployee())
                .isSameAs(employeeResponse);

        assertThat(result.getDepartment().getDepartmentCode())
                .isEqualTo("N/A");

        assertThat(result.getDepartment().getDepartmentName())
                .isEqualTo("Department Service Unavailable");

        assertThat(result.getDepartment().getLocation())
                .isEqualTo("N/A");

        assertThat(result.getDepartment().getHeadOfDepartment())
                .isEqualTo("N/A");

        assertThat(result.getDepartment().getStatus())
                .isEqualTo("UNAVAILABLE");
    }
}
