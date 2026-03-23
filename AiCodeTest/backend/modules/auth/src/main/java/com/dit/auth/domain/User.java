package com.dit.auth.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private String userId;
    private String userName;

    /** 비밀번호 — JSON 응답에서 노출되지 않도록 @JsonIgnore 적용 */
    @JsonIgnore
    private String password;
}
