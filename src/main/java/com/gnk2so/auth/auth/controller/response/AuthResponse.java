package com.gnk2so.auth.auth.controller.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthResponse {
    private String auth;
    private String refresh;
}
