package com.dit.user.dto;

import lombok.Data;

/**
 * 사용자 목록 조회 검색 조건 DTO
 */
@Data
public class UserSearchDto {
    private boolean includeDeleted; // 삭제된 사용자 포함 여부
    private String siteId;          // 사이트 ID (DEFAULT_SITE_ID)
    private String userId;          // 사용자 ID
}
