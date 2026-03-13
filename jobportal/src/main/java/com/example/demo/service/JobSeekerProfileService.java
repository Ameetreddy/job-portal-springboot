package com.example.demo.service;

import java.util.Optional;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.demo.entity.JobSeekerProfile;
import com.example.demo.entity.RecruiterProfile;
import com.example.demo.entity.User;
import com.example.demo.repository.JobSeekerProfileRepository;
import com.example.demo.repository.UserRepository;

@Service
public class JobSeekerProfileService {
	
	private final JobSeekerProfileRepository jobSeekerProfileRepository;
	private final UserRepository userRepository;
	

	public JobSeekerProfileService(JobSeekerProfileRepository jobSeekerProfileRepository,
			UserRepository userRepository) {
		
		this.jobSeekerProfileRepository = jobSeekerProfileRepository;
		this.userRepository = userRepository;
	}

	public Optional<JobSeekerProfile> getOne(Integer id){
		return jobSeekerProfileRepository.findById(id);
	}

	public JobSeekerProfile addNew(JobSeekerProfile jobSeekerProfile) {
		// TODO Auto-generated method stub
		return jobSeekerProfileRepository.save(jobSeekerProfile);
	}

	public JobSeekerProfile getCurrentSeekerProfile() {
		Authentication authentication=SecurityContextHolder.getContext().getAuthentication();
		if(!(authentication instanceof AnonymousAuthenticationToken)) {
				String currentUsername=authentication.getName();
				User user=userRepository.findByEmail(currentUsername).orElseThrow(()-> new UsernameNotFoundException("user not found"));
				
				Optional<JobSeekerProfile> seekerProfile=getOne(user.getUserId());
				return seekerProfile.orElse(null);
		}else return null;
		
		
		
	}
}