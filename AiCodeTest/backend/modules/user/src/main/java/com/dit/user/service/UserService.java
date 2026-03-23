package com.dit.user.service;

import com.dit.common.exception.BusinessException;
import com.dit.common.exception.ErrorCode;
import com.dit.user.domain.UserDomain;
import com.dit.user.dto.UserDto;
import com.dit.user.dto.UserSearchDto;
import com.dit.user.persistence.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 사용자 서비스 - 비즈니스 로직 및 트랜잭션 처리
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true) // 기본값: 읽기 전용 (쓰기 메서드는 @Transactional로 오버라이드)
public class UserService {

    private final UserMapper userMapper;
    private final BCryptPasswordEncoder passwordEncoder;

    /**
     * 사용자 목록 조회
     */
    public List<UserDomain> getUserList(UserSearchDto searchDto) {
        return userMapper.selectUserList(searchDto);
    }

    /**
     * 사용자 단건 조회
     */
    public UserDomain getUserById(String userId) {
        UserDomain user = userMapper.selectUserById(userId);
        if (user == null) {
            log.warn("사용자 조회 실패 - 존재하지 않는 사용자: userId={}", userId);
            throw new BusinessException(ErrorCode.USER_NOT_FOUND);
        }
        return user;
    }

    /**
     * 사용자 등록
     * [🔒보안] 비밀번호는 BCrypt 해싱 후 ENCODE_PASSWORD 컬럼에 저장 — 평문 저장 절대 금지
     * [🔒보안] 로그에 비밀번호 절대 출력 금지
     */
    @Transactional
    public void createUser(UserDto dto, String currentUserId) {
        log.info("사용자 등록 시작: userId={}, requestedBy={}", dto.getUserId(), currentUserId);

        /* 사용자명 필수 검증 (UserDto에서 @NotBlank 제거 — 수정 시 optional이므로 여기서 수동 검증) */
        if (dto.getUserName() == null || dto.getUserName().isBlank()) {
            throw new BusinessException(ErrorCode.INVALID_USER_DATA, "사용자명은 필수입니다.");
        }

        /* 비밀번호 확인 일치 검증 */
        if (dto.getPassword() == null || dto.getPassword().isBlank()) {
            throw new BusinessException(ErrorCode.INVALID_USER_DATA, "비밀번호는 필수입니다.");
        }
        if (!dto.getPassword().equals(dto.getPasswordConfirm())) {
            throw new BusinessException(ErrorCode.INVALID_USER_DATA, "비밀번호가 일치하지 않습니다.");
        }

        /* 중복 확인 */
        if (userMapper.countByUserId(dto.getUserId()) > 0) {
            log.warn("사용자 등록 실패 - 중복 ID: userId={}", dto.getUserId());
            throw new BusinessException(ErrorCode.USER_DUPLICATE);
        }

        UserDomain user = toUserDomain(dto);
        user.setCreatorId(currentUserId);

        // [🔒보안] BCrypt 해싱 후 ENCODE_PASSWORD에 저장 — 평문 저장 금지
        user.setEncodePassword(passwordEncoder.encode(dto.getPassword()));

        userMapper.insertUser(user);
        log.info("사용자 등록 완료: userId={}", dto.getUserId());
    }

    /**
     * 사용자 수정
     * [🔒보안] 비밀번호 변경 시 BCrypt 재해싱
     */
    @Transactional
    public void updateUser(String userId, UserDto dto, String currentUserId) {
        log.info("사용자 수정 시작: userId={}, requestedBy={}", userId, currentUserId);

        /* 존재 확인 */
        getUserById(userId);

        UserDomain user = toUserDomain(dto);
        user.setUserId(userId);
        user.setModifierId(currentUserId);

        /* 비밀번호 변경 시 BCrypt 재해싱 */
        if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
            if (!dto.getPassword().equals(dto.getPasswordConfirm())) {
                throw new BusinessException(ErrorCode.INVALID_USER_DATA, "비밀번호가 일치하지 않습니다.");
            }
            // [🔒보안] 새 비밀번호 BCrypt 해싱
            user.setEncodePassword(passwordEncoder.encode(dto.getPassword()));
        }

        userMapper.updateUser(user);
        log.info("사용자 수정 완료: userId={}", userId);
    }

    /**
     * 사용자 삭제
     */
    @Transactional
    public void deleteUser(String userId) {
        log.info("사용자 삭제 시작: userId={}", userId);
        /* 존재 확인 */
        getUserById(userId);
        userMapper.deleteUser(userId);
        log.info("사용자 삭제 완료: userId={}", userId);
    }

    /* ── DTO → Domain 변환 ── */
    private UserDomain toUserDomain(UserDto dto) {
        UserDomain u = new UserDomain();
        u.setUserId(dto.getUserId());
        u.setUserName(dto.getUserName());
        u.setChangePasswordFlag(dto.getChangePasswordFlag());
        u.setDepartmentId(dto.getDepartmentId());
        u.setDefaultSiteId(dto.getDefaultSiteId());
        u.setDefaultLanguageId(dto.getDefaultLanguageId());
        u.setPhoneOffice(dto.getPhoneOffice());
        u.setPhoneMobile(dto.getPhoneMobile());
        u.setPhoneHome(dto.getPhoneHome());
        u.setPhoneOther(dto.getPhoneOther());
        u.setEmailId(dto.getEmailId());
        u.setSexFlag(dto.getSexFlag());
        u.setBirthday(dto.getBirthday());
        u.setExpireDate(dto.getExpireDate());
        u.setPassExpireDate(dto.getPassExpireDate());
        u.setEnterDate(dto.getEnterDate());
        u.setRetireDate(dto.getRetireDate());
        u.setUserGroup1(dto.getUserGroup1());
        u.setUserGroup2(dto.getUserGroup2());
        u.setUserGroup3(dto.getUserGroup3());
        u.setUserGroup4(dto.getUserGroup4());
        u.setUserGroup5(dto.getUserGroup5());
        u.setUserGroup6(dto.getUserGroup6());
        u.setUserGroup7(dto.getUserGroup7());
        u.setUserGroup8(dto.getUserGroup8());
        u.setUserGroup9(dto.getUserGroup9());
        u.setUserGroup10(dto.getUserGroup10());
        return u;
    }
}
