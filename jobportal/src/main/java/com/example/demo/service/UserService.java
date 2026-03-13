package com.example.demo.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.entity.JobSeekerProfile;
import com.example.demo.entity.PendingUser;
import com.example.demo.entity.RecruiterProfile;
import com.example.demo.entity.User;
import com.example.demo.entity.VerificationToken;
import com.example.demo.repository.JobSeekerProfileRepository;
import com.example.demo.repository.PendingUserRepository;
import com.example.demo.repository.RecruiterProfileRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.repository.VerificationTokenRepository;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final JobSeekerProfileRepository jobSeekerProfileRepository;
    private final RecruiterProfileRepository recruiterProfileRepository;
    private final PasswordEncoder passwordEncoder;
   
    private VerificationTokenRepository verificationTokenRepository;
    private PendingUserRepository pendingUserRepository;
   
    private EmailService emailService;

    

    public void addNew(User user) {

       
        Optional<User> existingUser = userRepository.findByEmail(user.getEmail());
        if (existingUser.isPresent()) {
            throw new RuntimeException("Email already registered");
        }

        // Check if already pending
        Optional<PendingUser> existingPending =
                pendingUserRepository.findByEmail(user.getEmail());
        existingPending.ifPresent(pendingUserRepository::delete);

        // Create PendingUser
        PendingUser pendingUser = new PendingUser();
        pendingUser.setEmail(user.getEmail());
        pendingUser.setPassword(passwordEncoder.encode(user.getPassword()));
        
        // ⚡ FIXED PART: set userTypeId from form (integer)
        pendingUser.setUserType(user.getUserTypeId());

        pendingUser.setToken(java.util.UUID.randomUUID().toString());
        pendingUser.setExpiryDate(
                new java.util.Date(System.currentTimeMillis() + 1000 * 60 * 10)
        ); 

        pendingUserRepository.save(pendingUser);

       
        emailService.sendVerificationEmail(
                pendingUser.getEmail(),
                pendingUser.getToken()
        );
    }

    public UserService(UserRepository userRepository, JobSeekerProfileRepository jobSeekerProfileRepository,
			RecruiterProfileRepository recruiterProfileRepository, PasswordEncoder passwordEncoder,
			VerificationTokenRepository verificationTokenRepository, PendingUserRepository pendingUserRepository,
			EmailService emailService) {
		super();
		this.userRepository = userRepository;
		this.jobSeekerProfileRepository = jobSeekerProfileRepository;
		this.recruiterProfileRepository = recruiterProfileRepository;
		this.passwordEncoder = passwordEncoder;
		this.verificationTokenRepository = verificationTokenRepository;
		this.pendingUserRepository = pendingUserRepository;
		this.emailService = emailService;
	}

	public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

   
    public User getCurrentUser() {
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null ||
            authentication instanceof AnonymousAuthenticationToken) {
            throw new RuntimeException("User not logged in");
        }

        return userRepository.findByEmail(authentication.getName())
                .orElseThrow(() ->
                        new UsernameNotFoundException("Could not find user"));
    }

    
    @Transactional
    public Object getCurrentUserProfile() {

        User user = getCurrentUser();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // ---------- RECRUITER ----------
        if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_RECRUITER"))) {
            Optional<RecruiterProfile> existing =
                    recruiterProfileRepository.findByUserAccountId(user.getUserId());

            if (existing.isPresent()) {
                return existing.get();
            }

            RecruiterProfile recruiterProfile = new RecruiterProfile();
            recruiterProfile.setUserid(user);
            return recruiterProfileRepository.saveAndFlush(recruiterProfile);
        }

        // ---------- JOB SEEKER ----------
        Optional<JobSeekerProfile> existing =
                jobSeekerProfileRepository.findByUserAccountId(user.getUserId());

        if (existing.isPresent()) {
            return existing.get();
        }

        JobSeekerProfile jobSeekerProfile = new JobSeekerProfile();
        jobSeekerProfile.setUserId(user); // link user
       

        return jobSeekerProfileRepository.saveAndFlush(jobSeekerProfile);
    }


    public List<User> getUsersByType(String type) {
        return userRepository.findByUserTypeName(type);
    }

}
