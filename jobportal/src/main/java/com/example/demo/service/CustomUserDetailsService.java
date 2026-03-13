package com.example.demo.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.util.CustomUserDetails;

@Service
public class CustomUserDetailsService implements UserDetailsService {

	private final UserRepository userRepository;


	public CustomUserDetailsService(UserRepository userRepository)
	{
		this.userRepository=userRepository;
	}
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user=userRepository.findByEmail(username).orElseThrow(()->new UsernameNotFoundException("could not found user"));
		return new CustomUserDetails(user);
	}
}