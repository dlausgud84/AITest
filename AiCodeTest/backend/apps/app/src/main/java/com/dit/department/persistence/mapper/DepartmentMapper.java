package com.dit.department.persistence.mapper;

import com.dit.department.dto.DepartmentDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 부서 Mapper (NB_DEPARTMENTS)
 */
@Mapper
public interface DepartmentMapper {
    /** 부서 목록 조회 (SITE_ID 기준, DELETE_FLAG = 'N') */
    List<DepartmentDto> selectDepartmentList(@Param("siteId") String siteId);
}
