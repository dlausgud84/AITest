package com.dit.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 사용자 생성/수정 요청 DTO (NB_USERS 실제 컬럼 기준)
 */
@Data
public class UserDto {

    /* ── 기본 정보 ── */
    @NotBlank(message = "사용자 ID는 필수입니다.")
    @Size(min = 3, max = 50, message = "사용자 ID는 3~50자여야 합니다.")
    private String userId;

    // 수정 시 선택적 — createUser Service에서 null 검증 수행
    @Size(max = 100, message = "사용자명은 100자를 초과할 수 없습니다.")
    private String userName;

    // 비밀번호: 등록 시 필수, 수정 시 변경할 경우에만 입력
    @Size(min = 8, max = 100, message = "비밀번호는 8자 이상 100자 이하여야 합니다.")
    private String password;

    private String passwordConfirm;         // 비밀번호 확인 (검증은 Service에서 수행)
    private String changePasswordFlag;      // CHANGE_PASSWORD_FLAG (Y/N)
    private String departmentId;            // DEPARTMENT_ID
    private String defaultSiteId;           // DEFAULT_SITE_ID
    private String defaultLanguageId;       // DEFAULT_LANGUAGE_ID

    /* ── 연락처 ── */
    private String phoneOffice;             // PHONE_OFFICE
    private String phoneMobile;             // PHONE_MOBILE
    private String phoneHome;               // PHONE_HOME
    private String phoneOther;              // PHONE_OTHER

    @Email(message = "이메일 형식이 올바르지 않습니다.")
    private String emailId;                 // EMAIL_ID

    /* ── 상세 정보 ── */
    private String sexFlag;                 // SEX_FLAG (M/F)
    private String birthday;               // BIRTHDAY
    private String expireDate;             // EXPIRE_DATE (계정 만료)
    private String passExpireDate;         // PASS_EXPIRE_DATE (비밀번호 만료)
    private String enterDate;              // ENTER_DATE (입사일)
    private String retireDate;             // RETIRE_DATE (퇴사일)

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
