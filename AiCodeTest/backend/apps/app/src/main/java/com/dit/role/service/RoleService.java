package com.dit.role.service;

import com.dit.role.dto.RoleDto;
import com.dit.role.persistence.mapper.RoleMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 권한 서비스 (NB_ROLES)
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RoleService {

    private final RoleMapper roleMapper;

    /**
     * 사이트 권한 목록 조회
     * - SITE_ID 기준 필터
     * - SYSTEM_ROLE_FLAG != 'Y' (시스템 권한 제외)
     */
    public List<RoleDto> getRoleList(String siteId) {
        return roleMapper.selectRoleList(siteId);
    }
}
