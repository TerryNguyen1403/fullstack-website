package com.example.server.service;

import com.example.server.dto.UserLoginDTO;
import com.example.server.entity.User;
import com.example.server.exception.EmailNotFoundException;
import com.example.server.exception.InvalidPasswordException;
import com.example.server.repository.UserRepository;
import com.example.server.security.JwtTokenProvider;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Validated
@Service
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthService(UserRepository userRepository, JwtTokenProvider jwtTokenProvider, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.jwtTokenProvider = jwtTokenProvider;
        this.passwordEncoder = passwordEncoder;
    }

    public String login(UserLoginDTO request) {
        // Throw exception nếu user không tồn tại / chưa đăng ký
        User exist = userRepository.findByEmail(request.email()).orElseThrow(() -> new EmailNotFoundException("Email không tồn tại"));

        // Throw exception nếu mật khẩu không khớp
        if (!passwordEncoder.matches(request.password(), exist.getPassword()))
          throw new InvalidPasswordException("Sai mật khẩu");

      // Generate token
      return jwtTokenProvider.generateToken(request.email(), exist.getRole());
    }
}
