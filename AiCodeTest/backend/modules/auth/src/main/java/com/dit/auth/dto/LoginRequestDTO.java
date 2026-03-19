package com.dit.auth.dto;

import lombok.Data;

@Data
public class LoginRequestDTO {
    private String siteId;
    private String userId;
    private String password;
}
