package com.hrms.serviceimpl;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hrms.dto.JwtResponseDTO;
import com.hrms.dto.LoginDTO;
import com.hrms.dto.RegisterDTO;
import com.hrms.entity.Employee;
import com.hrms.entity.Role;
import com.hrms.entity.User;
import com.hrms.exception.DuplicateResourceException;
import com.hrms.exception.ResourceNotFoundException;
import com.hrms.repository.EmployeeRepository;
import com.hrms.repository.RoleRepository;
import com.hrms.repository.UserRepository;
import com.hrms.service.AuthService;
import com.hrms.util.JwtUtil;

@Service
public class AuthServiceImpl implements AuthService {

	private final UserRepository userRepository;
	private final RoleRepository roleRepository;
	private final EmployeeRepository employeeRepository;
	private final PasswordEncoder passwordEncoder;
	private final AuthenticationManager authenticationManager;
	private final JwtUtil jwtUtil;

	public AuthServiceImpl(UserRepository userRepository, RoleRepository roleRepository,
			EmployeeRepository employeeRepository, PasswordEncoder passwordEncoder,
			AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
		this.userRepository = userRepository;
		this.roleRepository = roleRepository;
		this.employeeRepository = employeeRepository;
		this.passwordEncoder = passwordEncoder;
		this.authenticationManager = authenticationManager;
		this.jwtUtil = jwtUtil;
	}

	@Override
	@Transactional
	public String registerUser(RegisterDTO registerDTO) {
		if (userRepository.existsByUsername(registerDTO.username())) {
			throw new DuplicateResourceException("Username '" + registerDTO.username() + "' already taken");
		}
		if (userRepository.existsByEmail(registerDTO.email())) {
			throw new DuplicateResourceException("Email '" + registerDTO.email() + "' already in use");
		}

		User user = new User();
		user.setUsername(registerDTO.username());
		user.setPassword(passwordEncoder.encode(registerDTO.password()));
		user.setEmail(registerDTO.email());
		user.setEnabled(true);

		Set<Role> roles = registerDTO.roles().stream()
				.map(roleName -> roleRepository.findByRoleName(Role.RoleName.valueOf(roleName))
						.orElseThrow(() -> new ResourceNotFoundException("Role not found: " + roleName)))
				.collect(Collectors.toSet());
		user.setRoles(roles);

		if (registerDTO.employeeId() != null) {
			Employee employee = employeeRepository.findById(registerDTO.employeeId())
					.orElseThrow(() -> new ResourceNotFoundException("Employee", "id", registerDTO.employeeId()));
			user.setEmployee(employee);
		}

		userRepository.save(user);
		return "User registered successfully";
	}

	@Override
	public JwtResponseDTO loginUser(LoginDTO loginDTO) {
		Authentication authentication = authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(loginDTO.username(), loginDTO.password()));

		List<String> roles = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority)
				.collect(Collectors.toList());

		String token = jwtUtil.generateToken(loginDTO.username(), roles);

		return new JwtResponseDTO(token, loginDTO.username(), roles);
	}
}