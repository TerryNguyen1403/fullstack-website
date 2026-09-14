package com.example.server.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
  // Inject JwtTokenProvider và UserDetailService sử dụng Constructor Injection
  private final JwtTokenProvider jwtTokenProvider;
  private final UserDetailsService userDetailsService;

  public JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider, UserDetailsService userDetailsService) {
    this.jwtTokenProvider = jwtTokenProvider;
    this.userDetailsService = userDetailsService;
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
    // 1. Lấy token từ Header "Authorization: Bearer <Token>
    String jwtToken = getTokenFromRequest(request);

    // 2. Nếu có token và token hợp lệ -> xác thực
    if (StringUtils.hasText(jwtToken) && jwtTokenProvider.validateToken(jwtToken)) {
      String email = jwtTokenProvider.getSubjectFromToken(jwtToken);
      log.info("XÁC THỰC THÀNH CÔNG CHO EMAIL {} - TOKEN {}", email, jwtToken);
      UserDetails userDetails = userDetailsService.loadUserByUsername(email);

      UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

      auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

      SecurityContextHolder.getContext().setAuthentication(auth);
    }

    filterChain.doFilter(request, response);
  }

  private String getTokenFromRequest(HttpServletRequest request) {
    String bearer = request.getHeader("Authorization");
    if (StringUtils.hasText(bearer) && bearer.startsWith("Bearer ")) {
      return bearer.substring(7);
    }
    return null;
  }
}
