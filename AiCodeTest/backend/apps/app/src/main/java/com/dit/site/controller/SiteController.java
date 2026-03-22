package com.dit.site.controller;

import com.dit.common.response.ApiResponse;
import com.dit.site.dto.SiteDto;
import com.dit.site.service.SiteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 사이트 API 컨트롤러 (NB_SITES)
 *
 * GET /api/sites - 사이트 목록 조회 (콤보박스용)
 */
@RestController
@RequestMapping("/api/sites")
public class SiteController {

    private final SiteService siteService;

    public SiteController(SiteService siteService) {
        this.siteService = siteService;
    }

    /** 사이트 목록 조회 (SITE_ID, SITE_NAME) */
    @GetMapping
    public ResponseEntity<ApiResponse<List<SiteDto>>> getSiteList() {
        List<SiteDto> list = siteService.getSiteList();
        return ResponseEntity.ok(ApiResponse.success("사이트 목록 조회 성공", list));
    }
}
