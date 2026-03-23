package com.dit.auth.controller;

import com.dit.auth.domain.Site;
import com.dit.auth.domain.User;
import com.dit.auth.dto.LoginRequestDTO;
import com.dit.auth.dto.LoginResponseDTO;
import com.dit.auth.dto.SiteDTO;
import com.dit.auth.service.AuthService;
import com.dit.common.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

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
     * [🔒보안] @Valid: 입력값 검증 (userId/password NotBlank) → 검증 실패 시 400 자동 반환
     * [🔒보안] 토큰은 응답에 포함되지만, 향후 JWT 방식으로 전환 필요
     */
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponseDTO>> login(@Valid @RequestBody LoginRequestDTO request) {
        User user = authService.login(request.getUserId(), request.getPassword());

        // TODO: 향후 UUID 토큰을 JWT(HS256)로 교체하여 서버리스 인증 구조로 개선 필요
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
