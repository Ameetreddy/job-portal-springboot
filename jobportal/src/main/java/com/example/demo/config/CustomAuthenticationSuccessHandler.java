package com.example.demo.config;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

	@Override
	public void onAuthenticationSuccess(
	        HttpServletRequest request,
	        HttpServletResponse response,
	        Authentication authentication)
	        throws IOException, ServletException {

	    boolean hasJobSeekerRole = authentication.getAuthorities()
	            .stream()
	            .anyMatch(a -> a.getAuthority().equals("ROLE_JOB_SEEKER"));

	    boolean hasRecruiterRole = authentication.getAuthorities()
	            .stream()
	            .anyMatch(a -> a.getAuthority().equals("ROLE_RECRUITER"));

	    if (hasRecruiterRole) {
	        response.sendRedirect("/dashboard/recruiter");
	        return;
	    }

	    if (hasJobSeekerRole) {
	        response.sendRedirect("/dashboard/jobseeker");
	        return;
	    }

	    // fallback (safety)
	    response.sendRedirect("/");
	}

}
