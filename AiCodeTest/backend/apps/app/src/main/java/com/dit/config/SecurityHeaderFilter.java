package com.dit.config;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * 보안 헤더 필터
 * Spring Security 미사용 환경에서 필수 보안 헤더를 수동으로 추가합니다.
 *
 * 적용 헤더:
 * - X-Content-Type-Options: nosniff        → MIME 타입 스니핑 방지
 * - X-Frame-Options: DENY                  → Clickjacking 방지
 * - X-XSS-Protection: 1; mode=block        → 구형 브라우저 XSS 필터 활성화
 * - Referrer-Policy: strict-origin-when-cross-origin → Referrer 정보 최소화
 */
@Component
public class SecurityHeaderFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletResponse httpResponse = (HttpServletResponse) response;

        // MIME 타입 스니핑 방지 — 브라우저가 Content-Type을 추측하지 않도록 강제
        httpResponse.setHeader("X-Content-Type-Options", "nosniff");

        // Clickjacking 방지 — iframe 내 페이지 삽입 차단
        httpResponse.setHeader("X-Frame-Options", "DENY");

        // 구형 브라우저 XSS 필터 활성화 (최신 브라우저는 CSP로 대체)
        httpResponse.setHeader("X-XSS-Protection", "1; mode=block");

        // Referrer 정보 최소화 — 크로스 도메인 요청 시 origin만 전달
        httpResponse.setHeader("Referrer-Policy", "strict-origin-when-cross-origin");

        chain.doFilter(request, response);
    }
}
