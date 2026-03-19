package com.dit.auth.controller;

import com.dit.auth.domain.Site;
import com.dit.auth.domain.User;
import com.dit.auth.dto.LoginRequestDTO;
import com.dit.auth.dto.LoginResponseDTO;
import com.dit.auth.dto.SiteDTO;
import com.dit.auth.service.AuthService;
import com.dit.common.response.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * 인증 API 컨트롤러
 *
 * 엔드포인트:
 * - GET  /api/auth/sites  - MES 공장(사이트) 목록 조회
 * - POST /api/auth/login  - 로그인
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /**
     * MES 공장 목록 조회
     */
    @GetMapping("/sites")
    public ResponseEntity<ApiResponse<List<SiteDTO>>> getSites() {
        List<Site> sites = authService.getMesSites();
        List<SiteDTO> siteDTOs = sites.stream()
                .map(s -> new SiteDTO(s.getSiteId(), s.getSiteName()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponse.success("공장 목록 조회 성공", siteDTOs));
    }

    /**
     * 로그인
     */
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponseDTO>> login(@RequestBody LoginRequestDTO request) {
        User user = authService.login(request.getUserId(), request.getPassword());
        String token = UUID.randomUUID().toString();
        LoginResponseDTO response = new LoginResponseDTO(
                user.getUserId(),
                user.getUserName(),
                request.getSiteId(),
                token
        );
        return ResponseEntity.ok(ApiResponse.success("로그인 성공", response));
    }
}
