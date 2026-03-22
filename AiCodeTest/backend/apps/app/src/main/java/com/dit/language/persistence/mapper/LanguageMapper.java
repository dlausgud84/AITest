package com.dit.language.persistence.mapper;

import com.dit.language.dto.LanguageDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 언어 Mapper (NB_LANGUAGES)
 */
@Mapper
public interface LanguageMapper {
    List<LanguageDto> selectLanguageList();
}
