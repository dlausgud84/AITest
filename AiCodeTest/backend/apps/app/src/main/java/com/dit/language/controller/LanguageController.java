package com.dit.language.controller;

import com.dit.common.response.ApiResponse;
import com.dit.language.dto.LanguageDto;
import com.dit.language.service.LanguageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 언어 API 컨트롤러 (NB_LANGUAGES)
 *
 * GET /api/languages - 언어 목록 조회 (팝업용)
 */
@RestController
@RequestMapping("/api/languages")
public class LanguageController {

    private final LanguageService languageService;

    public LanguageController(LanguageService languageService) {
        this.languageService = languageService;
    }

    /** 언어 목록 조회 (LANGUAGE_ID, LANGUAGE_NAME, IS_ENABLE_FLAG='Y') */
    @GetMapping
    public ResponseEntity<ApiResponse<List<LanguageDto>>> getLanguageList() {
        List<LanguageDto> list = languageService.getLanguageList();
        return ResponseEntity.ok(ApiResponse.success("언어 목록 조회 성공", list));
    }
}
