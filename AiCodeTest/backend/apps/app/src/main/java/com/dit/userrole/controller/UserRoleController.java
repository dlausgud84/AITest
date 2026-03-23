package com.dit.userrole.controller;

import com.dit.common.response.ApiResponse;
import com.dit.userrole.dto.UserRoleDto;
import com.dit.userrole.service.UserRoleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 사용자 권한 API 컨트롤러 (NB_USER_ROLES)
 *
 * GET /api/user-roles?siteId=XXX&userId=YYY - 사용자 권한 목록 조회
 */
@RestController
@RequestMapping("/api/user-roles")
public class UserRoleController {

    private final UserRoleService userRoleService;

    public UserRoleController(UserRoleService userRoleService) {
        this.userRoleService = userRoleService;
    }

    /**
     * 사용자 권한 목록 조회
     * - SITE_ID + USER_ID 기준 필터
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<UserRoleDto>>> getUserRoleList(
            @RequestParam(required = false) String siteId,
            @RequestParam(required = false) String userId) {
        List<UserRoleDto> list = userRoleService.getUserRoleList(siteId, userId);
        return ResponseEntity.ok(ApiResponse.success("사용자 권한 목록 조회 성공", list));
    }
}
