package com.dit.auth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class LoginRequestDTO {

    private String siteId;  // 공장 ID (선택)

    @NotBlank(message = "사용자 ID는 필수입니다.")
    @Size(max = 50, message = "사용자 ID는 50자를 초과할 수 없습니다.")
    private String userId;

    @NotBlank(message = "비밀번호는 필수입니다.")
    @Size(min = 1, max = 100, message = "비밀번호 형식이 올바르지 않습니다.")
    private String password;
}
