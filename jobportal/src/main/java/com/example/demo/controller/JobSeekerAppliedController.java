package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.demo.entity.JobSeekerApply;
import com.example.demo.entity.JobSeekerProfile;
import com.example.demo.entity.User;
import com.example.demo.service.JobSeekerApplyService;
import com.example.demo.service.JobSeekerProfileService;
import com.example.demo.service.UserService;

@Controller
public class JobSeekerAppliedController {

    private final JobSeekerApplyService jobSeekerApplyService;
    private final UserService userService;
    private final JobSeekerProfileService jobSeekerProfileService;

   
    public JobSeekerAppliedController(
            JobSeekerApplyService jobSeekerApplyService,
            UserService userService,
            JobSeekerProfileService jobSeekerProfileService) {

        this.jobSeekerApplyService = jobSeekerApplyService;
        this.userService = userService;
        this.jobSeekerProfileService = jobSeekerProfileService;
    }

    @GetMapping("/applied-jobs")
    public String viewAppliedJobs(Model model) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            return "redirect:/login";
        }

        String currentUsername = authentication.getName();

        User user = userService.getUserByEmail(currentUsername)
                .orElseThrow(() -> new RuntimeException("User not found"));

        JobSeekerProfile seekerProfile = jobSeekerProfileService.getOne(user.getUserId())
                .orElseThrow(() -> new RuntimeException("Job Seeker Profile not found"));

        List<JobSeekerApply> appliedList =
                jobSeekerApplyService.getCandidatesJobs(seekerProfile);

        model.addAttribute("appliedList", appliedList);
        model.addAttribute("user", userService.getCurrentUserProfile());

        return "applied-jobs"; 
    }
}
