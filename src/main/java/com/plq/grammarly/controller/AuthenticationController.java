package com.plq.grammarly.controller;

import com.plq.grammarly.model.AuthenticationRequest;
import com.plq.grammarly.model.AuthenticationResponse;
import com.plq.grammarly.model.MyUserDetails;
import com.plq.grammarly.service.impl.UserServiceImpl;
import com.plq.grammarly.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class AuthenticationController {

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtUtil jwtTokenUtil;

	@Autowired
	private UserServiceImpl userDetailsService;

	@PostMapping(value = "/login")
	// @CrossOrigin
    public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) throws Exception {
	HttpHeaders responseHeaders = new HttpHeaders();
		
		Authentication authenticate = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword())
		);

		//if authentication was succesful else throw an exception
		final MyUserDetails userDetails = (MyUserDetails) userDetailsService
			.loadUserByUsername(authenticationRequest.getUsername());
		final String jwt = jwtTokenUtil.generateToken(userDetails);
		AuthenticationResponse response = new AuthenticationResponse(jwt);

		response.setId(userDetails.getId());
		response.setUsername(userDetails.getUsername());
		List<String> roles = new ArrayList<String>();
		userDetails.getAuthorities().forEach((a) -> roles.add(a.getAuthority()));
		response.setRoles(roles);

		return new ResponseEntity<>(response, responseHeaders, HttpStatus.OK);
	
	}
	
}