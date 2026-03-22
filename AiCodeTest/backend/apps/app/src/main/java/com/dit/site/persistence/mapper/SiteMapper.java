package com.dit.site.persistence.mapper;

import com.dit.site.dto.SiteDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 사이트 Mapper (NB_SITES)
 */
@Mapper
public interface SiteMapper {
    List<SiteDto> selectSiteList();
}
