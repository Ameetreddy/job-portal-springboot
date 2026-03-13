package com.example.demo.controller;

import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.entity.PendingUser;
import com.example.demo.entity.User;
import com.example.demo.entity.VerificationToken;
import com.example.demo.repository.PendingUserRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.repository.UserTypeRepository;
import com.example.demo.repository.VerificationTokenRepository;

@Controller
public class VerificationController {

   
	private UserRepository userRepository;
	  
    private PendingUserRepository pendingUserRepository;

   
    public VerificationController(UserRepository userRepository, PendingUserRepository pendingUserRepository) {
		super();
		this.userRepository = userRepository;
		this.pendingUserRepository = pendingUserRepository;
	}


    @GetMapping("/verify")
    public String verifyAccount(@RequestParam("token") String token) {

        Optional<PendingUser> optionalPending =
                pendingUserRepository.findByToken(token);

        PendingUser pendingUser = optionalPending.get();

        User user = new User();
        user.setEmail(pendingUser.getEmail());
        user.setPassword(pendingUser.getPassword());
        user.setUserTypeId(pendingUser.getUserType()); 
        user.setActive(true);
        user.setRegistrationDate(new Date());

        userRepository.save(user);           
        pendingUserRepository.delete(pendingUser); 

        return "verification-success";
    }
}