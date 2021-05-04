package com.gnk2so.auth.auth.controller;

import java.security.Principal;

import javax.validation.Valid;

import com.gnk2so.auth.auth.controller.request.LoginRequest;
import com.gnk2so.auth.auth.controller.response.AuthResponse;
import com.gnk2so.auth.auth.service.AuthService;
import com.gnk2so.auth.provider.jwt.JwtProvider;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("/auth")
@Api(tags = "Authentication")
public class AuthController {
    
    @Autowired
    private AuthService service;

    @Autowired
    private JwtProvider jwtProvider;

    @PostMapping("/login")
    @ApiOperation(value = "Authenticate user")
    @ApiResponses({
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 400, message = "Bad Request"),
        @ApiResponse(code = 401, message = "Invalid/Expired token"),
    })
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        service.authenticate(request.getEmail(), request.getPassword());
        String authToken = jwtProvider.createAuthToken(request.getEmail());
        String refreshToken = jwtProvider.createRefreshToken(request.getEmail());
        return ResponseEntity.ok().body(new AuthResponse(authToken, refreshToken));
    }

    @GetMapping("/refresh")
    @ApiOperation(value = "Get new tokens")
    @ApiResponses({
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 401, message = "Invalid/Expired token"),
        @ApiResponse(code = 403, message = "Don't have permission"),
    })
    public ResponseEntity<AuthResponse> refreshTokens(Principal principal) {
        String authToken = jwtProvider.createAuthToken(principal.getName());
        String refreshToken = jwtProvider.createRefreshToken(principal.getName());
        return ResponseEntity.ok().body(new AuthResponse(authToken, refreshToken));
    }

}
