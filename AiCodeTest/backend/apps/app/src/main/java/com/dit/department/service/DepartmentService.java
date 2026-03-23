package com.dit.department.service;

import com.dit.department.dto.DepartmentDto;
import com.dit.department.persistence.mapper.DepartmentMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 부서 서비스 (NB_DEPARTMENTS)
 */
@Service
@Transactional(readOnly = true)
public class DepartmentService {

    private final DepartmentMapper departmentMapper;

    public DepartmentService(DepartmentMapper departmentMapper) {
        this.departmentMapper = departmentMapper;
    }

    /** 부서 목록 조회 (팝업용 - SITE_ID 기준, DELETE_FLAG = 'N') */
    public List<DepartmentDto> getDepartmentList(String siteId) {
        return departmentMapper.selectDepartmentList(siteId);
    }
}
