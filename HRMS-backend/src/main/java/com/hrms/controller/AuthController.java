package com.hrms.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hrms.dto.JwtResponseDTO;
import com.hrms.dto.LoginDTO;
import com.hrms.dto.RegisterDTO;
import com.hrms.service.AuthService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/auth")
@CrossOrigin("http://localhost:5173")
public class AuthController {

	private final AuthService authService;

	public AuthController(AuthService authService) {
		this.authService = authService;
	}

	@PostMapping("/register")
	public ResponseEntity<String> register(@Valid @RequestBody RegisterDTO registerDTO) {
		String message = authService.registerUser(registerDTO);
		return new ResponseEntity<>(message, HttpStatus.CREATED);
	}

	@PostMapping("/login")
	public ResponseEntity<JwtResponseDTO> login(@Valid @RequestBody LoginDTO loginDTO) {
		JwtResponseDTO response = authService.loginUser(loginDTO);
		return ResponseEntity.ok(response);
	}
}