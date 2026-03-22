package com.dit.user.service;

import com.dit.common.exception.BusinessException;
import com.dit.common.exception.ErrorCode;
import com.dit.user.domain.UserDomain;
import com.dit.user.dto.UserDto;
import com.dit.user.dto.UserSearchDto;
import com.dit.user.persistence.mapper.UserMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 사용자 서비스 - 비즈니스 로직 및 트랜잭션 처리
 */
@Service
@Transactional
public class UserService {

    private final UserMapper userMapper;

    public UserService(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    /**
     * 사용자 목록 조회
     */
    @Transactional(readOnly = true)
    public List<UserDomain> getUserList(UserSearchDto searchDto) {
        return userMapper.selectUserList(searchDto);
    }

    /**
     * 사용자 단건 조회
     */
    @Transactional(readOnly = true)
    public UserDomain getUserById(String userId) {
        UserDomain user = userMapper.selectUserById(userId);
        if (user == null) {
            throw new BusinessException(ErrorCode.USER_NOT_FOUND);
        }
        return user;
    }

    /**
     * 사용자 등록
     */
    public void createUser(UserDto dto, String currentUserId) {
        /* 필수 값 검증 */
        if (dto.getUserId() == null || dto.getUserId().isBlank()) {
            throw new BusinessException(ErrorCode.INVALID_USER_DATA, "사용자 ID는 필수입니다.");
        }
        if (dto.getUserName() == null || dto.getUserName().isBlank()) {
            throw new BusinessException(ErrorCode.INVALID_USER_DATA, "사용자명은 필수입니다.");
        }
        if (dto.getPassword() == null || dto.getPassword().isBlank()) {
            throw new BusinessException(ErrorCode.INVALID_USER_DATA, "비밀번호는 필수입니다.");
        }

        /* 비밀번호 확인 */
        if (!dto.getPassword().equals(dto.getPasswordConfirm())) {
            throw new BusinessException(ErrorCode.INVALID_USER_DATA, "비밀번호가 일치하지 않습니다.");
        }

        /* 중복 확인 */
        if (userMapper.countByUserId(dto.getUserId()) > 0) {
            throw new BusinessException(ErrorCode.USER_DUPLICATE);
        }

        UserDomain user = toUserDomain(dto);
        user.setCreatorId(currentUserId);
        userMapper.insertUser(user);
    }

    /**
     * 사용자 수정
     */
    public void updateUser(String userId, UserDto dto, String currentUserId) {
        /* 존재 확인 - getUserById 재사용 (없으면 BusinessException 자동 발생) */
        getUserById(userId);

        /* 비밀번호 변경 시 확인 */
        if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
            if (!dto.getPassword().equals(dto.getPasswordConfirm())) {
                throw new BusinessException(ErrorCode.INVALID_USER_DATA, "비밀번호가 일치하지 않습니다.");
            }
        }

        UserDomain user = toUserDomain(dto);
        user.setUserId(userId);
        user.setModifierId(currentUserId);
        userMapper.updateUser(user);
    }

    /**
     * 사용자 삭제
     */
    public void deleteUser(String userId) {
        /* 존재 확인 - getUserById 재사용 (없으면 BusinessException 자동 발생) */
        getUserById(userId);
        userMapper.deleteUser(userId);
    }

    /* ── DTO → Domain 변환 ── */
    private UserDomain toUserDomain(UserDto dto) {
        UserDomain u = new UserDomain();
        u.setUserId(dto.getUserId());
        u.setUserName(dto.getUserName());
        u.setPassword(dto.getPassword());
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
