package com.gongzone.central.member.login.controller;

import com.gongzone.central.member.login.domain.LoginRequest;
import com.gongzone.central.member.login.domain.LoginResponse;
import com.gongzone.central.member.login.service.LoginService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/login")
public class LoginController {

    private final LoginService loginService;

    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    @PostMapping
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        try {
            LoginResponse loginResponse = loginService.login(loginRequest);
            return ResponseEntity.ok(loginResponse);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }

    /*@PostMapping
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        LoginResponse loginResponse = loginService.login(loginRequest);
        return ResponseEntity.ok(loginResponse);
    }*/

    /*@PostMapping
    public ResponseEntity<Boolean> login(@RequestBody LoginRequest login) {
        Boolean loginResult = loginService.login(login);
        return ResponseEntity.ok(loginResult);
    }*/
}
