package com.hrms.service;

import com.hrms.dto.JwtResponseDTO;
import com.hrms.dto.LoginDTO;
import com.hrms.dto.RegisterDTO;

public interface AuthService {
	String registerUser(RegisterDTO registerDTO);

	JwtResponseDTO loginUser(LoginDTO loginDTO);
}