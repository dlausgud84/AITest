package com.dit.auth.service;

import com.dit.auth.domain.Site;
import com.dit.auth.domain.User;
import com.dit.auth.persistence.mapper.AuthMapper;
import com.dit.common.exception.BusinessException;
import com.dit.common.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {

    private final AuthMapper authMapper;

    /** MES 공장 목록 조회 */
    public List<Site> getMesSites() {
        return authMapper.selectMesSites();
    }

    /** 로그인 검증 (평문 비밀번호 비교) */
    @Transactional(readOnly = true)
    public User login(String userId, String password) {
        log.info("로그인 시도: userId={}", userId);

        User user = authMapper.selectUserById(userId);

        if (user == null || user.getPassword() == null
                || !user.getPassword().equals(password)) {
            log.warn("로그인 실패 (아이디/비밀번호 불일치): userId={}", userId);
            throw new BusinessException(ErrorCode.LOGIN_FAILED);
        }

        log.info("로그인 성공: userId={}", userId);
        return user;
    }
}
