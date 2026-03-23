package com.dit.userrole.persistence.mapper;

import com.dit.userrole.dto.UserRoleDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * NB_USER_ROLES 매퍼
 */
@Mapper
public interface UserRoleMapper {
    /**
     * 사용자 권한 목록 조회
     * - SITE_ID + USER_ID 기준 필터
     */
    List<UserRoleDto> selectUserRoleList(
            @Param("siteId") String siteId,
            @Param("userId") String userId);
}
