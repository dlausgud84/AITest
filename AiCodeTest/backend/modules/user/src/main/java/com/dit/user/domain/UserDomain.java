package com.dit.user.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 사용자 엔티티 (NB_USERS 테이블 실제 컬럼 기준 매핑)
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDomain {

    /* ── 기본 정보 ── */
    private String userId;              // USER_ID            (PK)
    private String userName;            // USER_NAME
    private String password;            // PASSWORD
    private String departmentId;        // DEPARTMENT_ID
    private String changePasswordFlag;  // CHANGE_PASSWORD_FLAG (Y/N)
    private String defaultSiteId;       // DEFAULT_SITE_ID
    private String defaultLanguageId;   // DEFAULT_LANGUAGE_ID

    /* ── 연락처 ── */
    private String phoneOffice;         // PHONE_OFFICE
    private String phoneMobile;         // PHONE_MOBILE
    private String phoneHome;           // PHONE_HOME
    private String phoneOther;          // PHONE_OTHER
    private String emailId;             // EMAIL_ID

    /* ── 상세 정보 ── */
    private String sexFlag;             // SEX_FLAG           (M/F)
    private String birthday;            // BIRTHDAY           (yyyyMMdd)
    private String expireDate;          // EXPIRE_DATE        (계정 만료, yyyyMMdd)
    private String passExpireDate;      // PASS_EXPIRE_DATE   (비밀번호 만료, yyyyMMdd)
    private String enterDate;           // ENTER_DATE         (입사일, yyyyMMdd)
    private String retireDate;          // RETIRE_DATE        (퇴사일, yyyyMMdd)

    /* ── 마지막 로그인 정보 ── */
    private String lastHostName;        // LAST_HOST_NAME
    private String lastLoginDttm;       // LAST_LOGIN_DTTM    (yyyyMMddHHmmss)
    private String lastIpv4Address;     // LAST_IPV4_ADDRESS
    private String lastIpv6Address;     // LAST_IPV6_ADDRESS
    private String lastMacAddress;      // LAST_MAC_ADDRESS
    private String lastMacAddress2;     // LAST_MAC_ADDRESS2

    /* ── 사용자 정의 그룹 ── */
    private String userGroup1;          // USER_GROUP1
    private String userGroup2;          // USER_GROUP2
    private String userGroup3;          // USER_GROUP3
    private String userGroup4;          // USER_GROUP4
    private String userGroup5;          // USER_GROUP5
    private String userGroup6;          // USER_GROUP6
    private String userGroup7;          // USER_GROUP7
    private String userGroup8;          // USER_GROUP8
    private String userGroup9;          // USER_GROUP9
    private String userGroup10;         // USER_GROUP10

    /* ── 삭제 정보 ── */
    private String deleteFlag;          // DELETE_FLAG        (Y/N)
    private String deleteDate;          // DELETE_DATE        (yyyyMMdd)
    private String deleteComment;       // DELETE_COMMENT

    /* ── 감사 컬럼 ── */
    private String creatorId;           // CREATOR_ID
    private String createDttm;          // CREATE_DTTM        (yyyyMMddHHmmss)
    private String modifierId;          // MODIFIER_ID
    private String modifyDttm;          // MODIFY_DTTM        (yyyyMMddHHmmss)
    private String encodePassword;      // ENCODE_PASSWORD
}
