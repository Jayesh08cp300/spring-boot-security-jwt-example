package com.example.service.impl;

import com.example.dto.request.SignInRequest;
import com.example.dto.request.SignUpRequest;
import com.example.dto.response.JwtAuthenticationResponse;
import com.example.entity.Role;
import com.example.entity.User;
import com.example.repository.UserRepository;
import com.example.service.AuthenticationService;
import com.example.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final JwtService jwtService;
	private final AuthenticationManager authenticationManager;

	@Override
	public JwtAuthenticationResponse signUp(SignUpRequest request) {
		var user = User.builder()
				.firstName(request.getFirstName())
				.lastName(request.getLastName())
				.email(request.getEmail())
				.password(passwordEncoder.encode(request.getPassword()))
				.role(Role.USER)
				.build();
		userRepository.save(user);
		return getJwtToken(user);
	}

	@Override
	public JwtAuthenticationResponse signIn(SignInRequest request) {
		authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
		var user = userRepository.findByEmail(request.getEmail())
				.orElseThrow(() -> new IllegalArgumentException("Invalid email or password."));
		return getJwtToken(user);
	}

	private JwtAuthenticationResponse getJwtToken(User user) {
		return JwtAuthenticationResponse.builder()
				.token(jwtService.generateToken(user))
				.build();
	}
}
