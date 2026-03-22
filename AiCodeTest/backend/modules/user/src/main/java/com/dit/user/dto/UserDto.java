package com.dit.user.dto;

import lombok.Data;

/**
 * 사용자 생성/수정 요청 DTO (NB_USERS 실제 컬럼 기준)
 */
@Data
public class UserDto {

    /* ── 기본 정보 ── */
    private String userId;
    private String userName;
    private String password;
    private String passwordConfirm;
    private String changePasswordFlag;  // CHANGE_PASSWORD_FLAG (Y/N)
    private String departmentId;        // DEPARTMENT_ID
    private String defaultSiteId;       // DEFAULT_SITE_ID
    private String defaultLanguageId;   // DEFAULT_LANGUAGE_ID

    /* ── 연락처 ── */
    private String phoneOffice;         // PHONE_OFFICE
    private String phoneMobile;         // PHONE_MOBILE
    private String phoneHome;           // PHONE_HOME
    private String phoneOther;          // PHONE_OTHER
    private String emailId;             // EMAIL_ID

    /* ── 상세 정보 ── */
    private String sexFlag;             // SEX_FLAG (M/F)
    private String birthday;            // BIRTHDAY
    private String expireDate;          // EXPIRE_DATE (계정 만료)
    private String passExpireDate;      // PASS_EXPIRE_DATE (비밀번호 만료)
    private String enterDate;           // ENTER_DATE (입사일)
    private String retireDate;          // RETIRE_DATE (퇴사일)

    /* ── 사용자 정의 그룹 ── */
    private String userGroup1;
    private String userGroup2;
    private String userGroup3;
    private String userGroup4;
    private String userGroup5;
    private String userGroup6;
    private String userGroup7;
    private String userGroup8;
    private String userGroup9;
    private String userGroup10;
}
