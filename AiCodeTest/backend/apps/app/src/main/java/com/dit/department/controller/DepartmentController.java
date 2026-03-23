package com.dit.department.controller;

import com.dit.common.response.ApiResponse;
import com.dit.department.dto.DepartmentDto;
import com.dit.department.service.DepartmentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 부서 API 컨트롤러 (NB_DEPARTMENTS)
 *
 * GET /api/departments?siteId=XXX - 부서 목록 조회 (팝업용)
 */
@RestController
@RequestMapping("/api/departments")
public class DepartmentController {

    private final DepartmentService departmentService;

    public DepartmentController(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    /**
     * 부서 목록 조회 (팝업용)
     * - SITE_ID 기준 필터
     * - DELETE_FLAG = 'N' 조건
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<DepartmentDto>>> getDepartmentList(
            @RequestParam(required = false) String siteId) {
        List<DepartmentDto> list = departmentService.getDepartmentList(siteId);
        return ResponseEntity.ok(ApiResponse.success("부서 목록 조회 성공", list));
    }
}
