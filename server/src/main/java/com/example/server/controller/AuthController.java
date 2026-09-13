package com.example.server.controller;

import com.example.server.dto.UserLoginDTO;
import com.example.server.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
  private final AuthService authService;

  public AuthController(AuthService authService) {
    this.authService = authService;
  }

  @PostMapping("/log-in")
  public String login(@Valid @RequestBody UserLoginDTO request) {
    return authService.login(request);
  }
}
