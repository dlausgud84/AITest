package com.dit.site.service;

import com.dit.site.dto.SiteDto;
import com.dit.site.persistence.mapper.SiteMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 사이트 서비스 (NB_SITES)
 */
@Service
@Transactional(readOnly = true)
public class SiteService {

    private final SiteMapper siteMapper;

    public SiteService(SiteMapper siteMapper) {
        this.siteMapper = siteMapper;
    }

    /** 사이트 목록 조회 (콤보박스용) */
    public List<SiteDto> getSiteList() {
        return siteMapper.selectSiteList();
    }
}
