package com.example.service;

import com.example.dto.request.SignInRequest;
import com.example.dto.request.SignUpRequest;
import com.example.dto.response.JwtAuthenticationResponse;

public interface AuthenticationService {
	JwtAuthenticationResponse signUp(SignUpRequest request);

	JwtAuthenticationResponse signIn(SignInRequest request);
}
