package com.example.server.service;

import com.example.server.dto.LoginRequestDTO;
import com.example.server.dto.LoginResponseDTO;
import com.example.server.entity.enums.Role;
import com.example.server.repository.UserRepository;
import com.example.server.security.JwtTokenProvider;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Validated
@Service
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthService(JwtTokenProvider jwtTokenProvider, AuthenticationManager authenticationManager) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.authenticationManager = authenticationManager;
    }

    public LoginResponseDTO login(LoginRequestDTO request) {
        // 1. Đóng gói email + password thành một "yêu cầu xác thực"
        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(request.email(), request.password());

        // 2. Giao cho AuthenticationManager kiểm tra,
        //      nếu sai throw BadCredentialException
        Authentication authentication = authenticationManager.authenticate(authToken);

        // 3. Xác thực thành công, lấy user đã được nạp
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        assert userDetails != null;
        String authority = userDetails.getAuthorities().stream()
                .findFirst().map(GrantedAuthority::getAuthority).orElseThrow();

        Role role = Role.valueOf(authority.replace("ROLE_", ""));

        // 4. Sinh Jwt Token
        String jwt = jwtTokenProvider.generateToken(userDetails.getUsername(), role);
        long expiresIn = jwtTokenProvider.getExpirationMs();

        return new LoginResponseDTO(jwt, expiresIn);
    }

    public boolean validateToken(String token) {
      return jwtTokenProvider.validateToken(token);
    }
}
