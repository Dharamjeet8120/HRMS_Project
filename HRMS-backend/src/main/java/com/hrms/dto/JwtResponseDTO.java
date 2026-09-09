package com.hrms.dto;

import java.util.List;

public record JwtResponseDTO(String token, String username, List<String> roles) {
}