package com.dit.auth.persistence.mapper;

import com.dit.auth.domain.Site;
import com.dit.auth.domain.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AuthMapper {

    /** MES 공장(사이트) 목록 조회 */
    List<Site> selectMesSites();

    /**
     * 사용자 ID로 사용자 조회 (비밀번호 검증은 Service 계층에서 BCrypt로 수행)
     * [🔒보안] 비밀번호를 SQL WHERE 조건에 직접 포함하지 않음
     */
    User selectUserById(@Param("userId") String userId);
}
