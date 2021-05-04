package com.gnk2so.auth.user.mock;

import com.gnk2so.auth.user.controller.request.SaveUserRequest;

public class SaveUserRequestMock {
    
    public static SaveUserRequest build() {
        return SaveUserRequest.builder()
            .name("New User")
            .email("new.user@email.com")
            .password("p@ssw0rd")
            .build();
    }

}
