package com.dit.site.dto;

import lombok.Data;

/**
 * 사이트 조회 DTO (NB_SITES)
 */
@Data
public class SiteDto {
    private String siteId;    // SITE_ID
    private String siteName;  // SITE_NAME
}
