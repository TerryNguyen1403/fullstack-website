package com.example.server.dto;

public record LoginResponseDTO(
  String token,
  String type,       // luôn là "Bearer"
  long expiresIn     // token sống bao lâu (mili-giây)
) {
  // constructor tiện lợi: chỉ cần truyền token và thời hạn
  public LoginResponseDTO(String token, long expiresIn) {
    this(token, "Bearer", expiresIn);
  }
}

