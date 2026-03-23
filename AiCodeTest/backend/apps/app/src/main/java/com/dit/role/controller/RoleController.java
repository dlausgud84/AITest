package com.dit.role.controller;

import com.dit.common.response.ApiResponse;
import com.dit.role.dto.RoleDto;
import com.dit.role.service.RoleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 권한 API 컨트롤러 (NB_ROLES)
 *
 * GET /api/roles?siteId=XXX - 사이트 권한 목록 조회
 */
@RestController
@RequestMapping("/api/roles")
public class RoleController {

    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    /**
     * 사이트 권한 목록 조회
     * - SITE_ID 기준 필터, SYSTEM_ROLE_FLAG != 'Y'
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<RoleDto>>> getRoleList(
            @RequestParam(required = false) String siteId) {
        List<RoleDto> list = roleService.getRoleList(siteId);
        return ResponseEntity.ok(ApiResponse.success("권한 목록 조회 성공", list));
    }
}
