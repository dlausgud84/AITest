package com.dit.language.service;

import com.dit.language.dto.LanguageDto;
import com.dit.language.persistence.mapper.LanguageMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 언어 서비스 (NB_LANGUAGES)
 */
@Service
@Transactional(readOnly = true)
public class LanguageService {

    private final LanguageMapper languageMapper;

    public LanguageService(LanguageMapper languageMapper) {
        this.languageMapper = languageMapper;
    }

    /** 언어 목록 조회 (팝업용 - IS_ENABLE_FLAG = 'Y' 필터) */
    public List<LanguageDto> getLanguageList() {
        return languageMapper.selectLanguageList();
    }
}
