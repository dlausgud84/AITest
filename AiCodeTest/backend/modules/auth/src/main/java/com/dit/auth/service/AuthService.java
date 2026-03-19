package com.dit.auth.service;

import com.dit.auth.domain.Site;
import com.dit.auth.domain.User;
import com.dit.auth.persistence.mapper.AuthMapper;
import com.dit.common.exception.BusinessException;
import com.dit.common.exception.ErrorCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class AuthService {

    private final AuthMapper authMapper;

    public AuthService(AuthMapper authMapper) {
        this.authMapper = authMapper;
    }

    @Transactional(readOnly = true)
    public List<Site> getMesSites() {
        return authMapper.selectMesSites();
    }

    @Transactional(readOnly = true)
    public User login(String userId, String password) {
        User user = authMapper.selectUserByIdAndPassword(userId, password);
        if (user == null) {
            throw new BusinessException(ErrorCode.LOGIN_FAILED);
        }
        return user;
    }
}
