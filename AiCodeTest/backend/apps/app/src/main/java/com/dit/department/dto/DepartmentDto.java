package com.dit.department.dto;

import lombok.Data;

/**
 * 부서 조회 DTO (NB_DEPARTMENTS)
 */
@Data
public class DepartmentDto {
    private String departmentId;    // DEPARTMENT_ID
    private String departmentName;  // DEPARTMENT_NAME
}
