package com.dit.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * 보안 설정
 * BCryptPasswordEncoder를 Bean으로 등록하여 비밀번호 해싱에 사용합니다.
 * Spring Security 전체를 사용하지 않고 crypto 모듈만 활용합니다.
 */
@Configuration
public class SecurityConfig {

    /**
     * BCryptPasswordEncoder Bean 등록
     * - strength 기본값 10 (권장) — 값이 높을수록 안전하지만 연산 비용 증가
     * - 비밀번호 저장/검증 시 반드시 이 Bean을 주입받아 사용할 것
     */
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
