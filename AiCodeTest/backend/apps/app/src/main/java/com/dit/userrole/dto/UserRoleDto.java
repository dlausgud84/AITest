package com.dit.userrole.dto;

import lombok.Data;

/**
 * 사용자 권한 조회 DTO (NB_USER_ROLES)
 * - NB_USERS  LEFT JOIN → userName(사용자명)
 * - NB_ROLES  LEFT JOIN → roleName(권한명)
 */
@Data
public class UserRoleDto {
    private String userId;       // USER_ID
    private String userName;     // NB_USERS 조인 결과 (USER_NAME)
    private String roleId;       // ROLE_ID
    private String roleName;     // NB_ROLES 조인 결과 (ROLE_NAME)
    private String description;  // DESCRIPTION
}
