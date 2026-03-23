package com.dit.role.dto;

import lombok.Data;

/**
 * 권한 조회 DTO (NB_ROLES)
 */
@Data
public class RoleDto {
    private String siteId;    // SITE_ID
    private String roleId;    // ROLE_ID
    private String roleName;  // ROLE_NAME
}
