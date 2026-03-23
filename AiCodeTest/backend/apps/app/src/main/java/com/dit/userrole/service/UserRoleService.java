package com.dit.userrole.service;

import com.dit.userrole.dto.UserRoleDto;
import com.dit.userrole.persistence.mapper.UserRoleMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 사용자 권한 서비스 (NB_USER_ROLES)
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserRoleService {

    private final UserRoleMapper userRoleMapper;

    /**
     * 사용자 권한 목록 조회
     * - SITE_ID + USER_ID 기준 필터
     */
    public List<UserRoleDto> getUserRoleList(String siteId, String userId) {
        return userRoleMapper.selectUserRoleList(siteId, userId);
    }
}
