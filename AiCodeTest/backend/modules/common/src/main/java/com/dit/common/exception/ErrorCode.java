package com.dit.common.exception;

/**
 * 시스템 에러 코드 정의
 */
public enum ErrorCode {
    // 공통 에러
    INVALID_REQUEST("E001", "잘못된 요청입니다."),
    INTERNAL_SERVER_ERROR("E002", "시스템 오류가 발생했습니다."),
    RESOURCE_NOT_FOUND("E003", "요청한 리소스를 찾을 수 없습니다."),
    UNAUTHORIZED("E004", "인증되지 않은 요청입니다."),
    FORBIDDEN("E005", "접근 권한이 없습니다."),

    // 인증 관련 에러
    LOGIN_FAILED("A001", "아이디 또는 비밀번호가 일치하지 않습니다."),

    // 메뉴 관련 에러
    MENU_NOT_FOUND("M001", "메뉴를 찾을 수 없습니다."),
    MENU_DUPLICATE("M002", "이미 존재하는 메뉴입니다."),
    INVALID_MENU_DATA("M003", "유효하지 않은 메뉴 데이터입니다."),

    // 사용자 관련 에러
    USER_NOT_FOUND("U001", "사용자를 찾을 수 없습니다."),
    USER_DUPLICATE("U002", "이미 존재하는 사용자 ID입니다."),
    INVALID_USER_DATA("U003", "유효하지 않은 사용자 데이터입니다."),

    // 입력값 검증 에러
    INVALID_INPUT("V001", "입력값이 올바르지 않습니다."),
    INVALID_SORT_COLUMN("V002", "허용되지 않은 정렬 컬럼입니다.");

    private final String code;
    private final String message;

    ErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
