package com.dit.auth.persistence.mapper;

import com.dit.auth.domain.Site;
import com.dit.auth.domain.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AuthMapper {
    List<Site> selectMesSites();
    User selectUserByIdAndPassword(@Param("userId") String userId, @Param("password") String password);
}
