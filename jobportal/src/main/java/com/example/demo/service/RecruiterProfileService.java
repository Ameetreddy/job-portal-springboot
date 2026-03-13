package com.example.demo.service;

import java.util.Optional;


import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


import com.example.demo.entity.RecruiterProfile;
import com.example.demo.entity.User;
import com.example.demo.repository.RecruiterProfileRepository;
import com.example.demo.repository.UserRepository;

@Service
public class RecruiterProfileService {

	private final RecruiterProfileRepository recruiterProfileRepository;
	private final UserRepository userRepository;
	
	public RecruiterProfileService(RecruiterProfileRepository recruiterProfileRepository,
			UserRepository userRepository) {
		super();
		this.recruiterProfileRepository = recruiterProfileRepository;
		this.userRepository = userRepository;
	}
	
	public Optional<RecruiterProfile> getOne(Integer id)
	{
		return recruiterProfileRepository.findById(id);
	}

	

	public RecruiterProfile addNew(RecruiterProfile recruiterProfile) {
		// TODO Auto-generated method stub
		return recruiterProfileRepository.save(recruiterProfile);
	}

	public RecruiterProfile getCurrentRecruiterProfile() {
		Authentication authentication=SecurityContextHolder.getContext().getAuthentication();
		if(!(authentication instanceof AnonymousAuthenticationToken)) {
			String currentUsername=authentication.getName();
			User user=userRepository.findByEmail(currentUsername).orElseThrow(()-> new UsernameNotFoundException("user not found"));
			
			Optional<RecruiterProfile> recruiterProfile=getOne(user.getUserId());
			return recruiterProfile.orElse(null);
	}else return null;
	
	}
	
}
