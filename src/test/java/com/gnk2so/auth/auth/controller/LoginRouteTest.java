package com.gnk2so.auth.auth.controller;

import com.gnk2so.auth.ControllerTest;
import com.gnk2so.auth.auth.controller.request.LoginRequest;
import com.gnk2so.auth.auth.exception.InvalidCredentialsException;
import com.gnk2so.auth.auth.mock.LoginRequestMock;
import com.gnk2so.auth.provider.jwt.JwtProvider;
import com.gnk2so.auth.user.mock.UserMock;
import com.gnk2so.auth.user.model.User;
import com.gnk2so.auth.user.repository.UserRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

public class LoginRouteTest extends ControllerTest {

    @Autowired
    private UserRepository repository;

    @MockBean
    private JwtProvider jwtProvider;

    private final String ROUTE = "/auth/login";

    @BeforeEach
    public void setup() {
        repository.deleteAll();
    }

    @Test
    public void shouldReturnStatusCodeOkAndTokensWhenAuthenticateSuccessfully() throws Exception {
        
        User user = UserMock.build();
        LoginRequest content = LoginRequestMock.build(user.getEmail(), user.getPassword());

        repository.save(UserMock.buildSecuredFrom(user));
        when(jwtProvider.createAuthToken(anyString())).thenReturn("AUTH");
        when(jwtProvider.createRefreshToken(anyString())).thenReturn("REFRESH");

        doPostRequest(ROUTE, content)
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.auth").value("AUTH"))
            .andExpect(jsonPath("$.refresh").value("REFRESH"));

    }

    @Test
    public void shouldReturnStatusCodeBadRequestAndErroMessageWhenSendRequestWithNullEmail() throws Exception {
        
        User user = UserMock.build();
        LoginRequest content = LoginRequestMock.build(null, user.getPassword());

        doPostRequest(ROUTE, content)
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.status").value(BAD_REQUEST.value()))
            .andExpect(jsonPath("$.errors[0].error").value("must not be null"));
    }

    @Test
    public void shouldReturnStatusCodeBadRequestAndErroMessageWhenSendRequestWithInvalidEmail() throws Exception {
        
        User user = UserMock.build();
        String bigEmail = "ThisEmailHasLengthGreaterThanSixtyFourCharacters.gnk2so@email.com.br";
        LoginRequest content = LoginRequestMock.build(bigEmail, user.getPassword());

        doPostRequest(ROUTE, content)
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.status").value(BAD_REQUEST.value()))
            .andExpect(jsonPath("$.errors[0].error").value("size must be lower than 64"));
    }

    @Test
    public void shouldReturnStatusCodeBadRequestAndErroMessageWhenSendRequestWithNullPassword() throws Exception {
        
        User user = UserMock.build();
        LoginRequest content = LoginRequestMock.build(user.getEmail(), null);

        doPostRequest(ROUTE, content)
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.status").value(BAD_REQUEST.value()))
            .andExpect(jsonPath("$.errors[0].error").value("must not be null"));
    }

    @Test
    public void shouldReturnStatusCodeBadRequestAndErroMessageWhenSendRequestWithPasswordWithLenghtLowerThanSix() throws Exception {
        
        User user = UserMock.build();
        LoginRequest content = LoginRequestMock.build(user.getEmail(), "FOUR");

        doPostRequest(ROUTE, content)
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.status").value(BAD_REQUEST.value()))
            .andExpect(jsonPath("$.errors[0].error").value("size must be between 6 and 24"));
    }

    @Test
    public void shouldReturnStatusCodeBadRequestAndErroMessageWhenSendRequestWithPasswordWithLenghtGreaterThanTwentyFour() throws Exception {
        
        User user = UserMock.build();
        String bigPassword = "ThisPasswordIsGreaterThenTwentyFourCharacters";
        LoginRequest content = LoginRequestMock.build(user.getEmail(), bigPassword);

        doPostRequest(ROUTE, content)
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.status").value(BAD_REQUEST.value()))
            .andExpect(jsonPath("$.errors[0].error").value("size must be between 6 and 24"));
    }

    @Test
    public void shouldReturnStatusCodeBadRequestAndErroMessageWhenSendRequestWithEmailLengthGreaterThanSixtyFour() throws Exception {
        
        User user = UserMock.build();
        LoginRequest content = LoginRequestMock.build("invalid.email@", user.getPassword());

        doPostRequest(ROUTE, content)
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.status").value(BAD_REQUEST.value()))
            .andExpect(jsonPath("$.errors[0].error").value("must be a well-formed email address"));
    }

    @Test
    public void shouldReturnStatusCodeUnauthorizedAndErroMessageWhenAuthenticationFailure() throws Exception {
        
        User user = UserMock.build();
        LoginRequest content = LoginRequestMock.build(user.getEmail(), user.getPassword());

        doPostRequest(ROUTE, content)
            .andExpect(status().isUnauthorized())
            .andExpect(jsonPath("$.status").value(UNAUTHORIZED.value()))
            .andExpect(jsonPath("$.message").value(InvalidCredentialsException.MESSAGE));
    }
}
