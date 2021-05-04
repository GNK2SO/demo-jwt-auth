package com.gnk2so.auth.auth.mock;

import com.gnk2so.auth.auth.controller.request.LoginRequest;

public class LoginRequestMock {

	public static LoginRequest build(String email, String password) {
		return LoginRequest.builder()
            .email(email)
            .password(password)
            .build();
	}
    
}
