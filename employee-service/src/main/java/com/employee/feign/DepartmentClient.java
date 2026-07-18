package com.employee.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.employee.dto.response.DepartmentResponse;

@FeignClient(
        name = "department-service"
)
public interface DepartmentClient {

    @GetMapping("/api/v1/departments/code/{departmentCode}")
    DepartmentResponse getDepartmentByCode(
            @PathVariable("departmentCode") String departmentCode);

}