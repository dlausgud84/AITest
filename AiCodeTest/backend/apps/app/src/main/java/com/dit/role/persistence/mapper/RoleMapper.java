package com.dit.role.persistence.mapper;

import com.dit.role.dto.RoleDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * NB_ROLES 매퍼
 */
@Mapper
public interface RoleMapper {
    /**
     * 사이트 권한 목록 조회
     * - SITE_ID 기준 필터
     * - SYSTEM_ROLE_FLAG != 'Y' (시스템 권한 제외)
     */
    List<RoleDto> selectRoleList(@Param("siteId") String siteId);
}
