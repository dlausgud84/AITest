package com.dit.user.persistence.mapper;

import com.dit.user.domain.UserDomain;
import com.dit.user.dto.UserSearchDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 사용자 MyBatis Mapper 인터페이스 (NB_USERS)
 */
@Mapper
public interface UserMapper {

    /** 사용자 목록 조회 (검색 조건 포함) */
    List<UserDomain> selectUserList(UserSearchDto searchDto);

    /** 사용자 단건 조회 */
    UserDomain selectUserById(String userId);

    /** 사용자 등록 */
    int insertUser(UserDomain user);

    /** 사용자 수정 */
    int updateUser(UserDomain user);

    /** 사용자 삭제 */
    int deleteUser(String userId);

    /** 사용자 ID 중복 확인 */
    int countByUserId(String userId);
}
