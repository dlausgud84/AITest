package com.dit.language.dto;

import lombok.Data;

/**
 * 언어 조회 DTO (NB_LANGUAGES)
 */
@Data
public class LanguageDto {
    private String languageId;    // LANGUAGE_ID
    private String languageName;  // LANGUAGE_NAME
}
