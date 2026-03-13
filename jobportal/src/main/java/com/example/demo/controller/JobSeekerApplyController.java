package com.example.demo.controller;


import java.util.Date;
import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.demo.entity.JobPostActivity;
import com.example.demo.entity.JobSeekerApply;
import com.example.demo.entity.JobSeekerProfile;
import com.example.demo.entity.JobSeekerSave;
import com.example.demo.entity.RecruiterProfile;

import com.example.demo.entity.User;
import com.example.demo.service.JobPostActivityService;
import com.example.demo.service.JobSeekerApplyService;
import com.example.demo.service.JobSeekerProfileService;
import com.example.demo.service.JobSeekerSaveService;
import com.example.demo.service.RecruiterProfileService;
import com.example.demo.service.UserService;

@Controller
public class JobSeekerApplyController {

    private final JobPostActivityService jobPostActivityService;
    private final UserService userService;
    private final JobSeekerApplyService jobSeekerApplyService;
    private final JobSeekerSaveService jobSeekerSaveService;
    private final RecruiterProfileService recruiterProfileService;
    private final JobSeekerProfileService jobSeekerProfileService;

  
    public JobSeekerApplyController(JobPostActivityService jobPostActivityService,
                                    UserService userService,
                                    JobSeekerApplyService jobSeekerApplyService,
                                    JobSeekerSaveService jobSeekerSaveService,
                                    RecruiterProfileService recruiterProfileService,
                                    JobSeekerProfileService jobSeekerProfileService) {

        this.jobPostActivityService = jobPostActivityService;
        this.userService = userService;
        this.jobSeekerApplyService = jobSeekerApplyService;
        this.jobSeekerSaveService = jobSeekerSaveService;
        this.recruiterProfileService = recruiterProfileService;
        this.jobSeekerProfileService = jobSeekerProfileService;
    }



    @GetMapping("job-details-apply/{id}")
    public String display(@PathVariable("id") int id, Model model) {

        JobPostActivity jobDetails = jobPostActivityService.getOne(id);
        List<JobSeekerApply> appliedList = jobSeekerApplyService.getJobCandidates(jobDetails);
        List<JobSeekerSave> savedList = jobSeekerSaveService.getJobCandidates(jobDetails);

        boolean alreadyApplied = false;
        boolean alreadySaved = false;

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (!(authentication instanceof AnonymousAuthenticationToken)) {

            if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("Recruiter"))) {

                RecruiterProfile recruiter = recruiterProfileService.getCurrentRecruiterProfile();
                if (recruiter != null) {
                    model.addAttribute("applyList", appliedList);
                }

            } else {

                JobSeekerProfile seeker = jobSeekerProfileService.getCurrentSeekerProfile();
                if (seeker != null) {

                    alreadyApplied = appliedList.stream()
                            .anyMatch(a -> a.getUserId().getUserAccountId()
                                    == seeker.getUserAccountId());

                    alreadySaved = savedList.stream()
                            .anyMatch(s -> s.getUserId().getUserAccountId()
                                    == seeker.getUserAccountId());
                }
            }
        }

        model.addAttribute("alreadyApplied", alreadyApplied);
        model.addAttribute("alreadySaved", alreadySaved);
        model.addAttribute("jobDetails", jobDetails);
        model.addAttribute("user", userService.getCurrentUserProfile());

        return "job-details";
    }

  

    @PostMapping("job-details/apply/{id}")
    public String apply(@PathVariable("id") int id) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication instanceof AnonymousAuthenticationToken) {
            throw new RuntimeException("User not authenticated");
        }

        String email = authentication.getName();
        User user = userService.getUserByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        JobSeekerProfile seekerProfile = jobSeekerProfileService.getOne(user.getUserId())
                .orElseThrow(() -> new RuntimeException("Job Seeker profile not found"));

        JobPostActivity job = jobPostActivityService.getOne(id);

        List<JobSeekerApply> appliedList = jobSeekerApplyService.getJobCandidates(job);

     // Check if current user already applied
     boolean alreadyApplied = appliedList.stream()
             .anyMatch(a -> a.getUserId().getUserAccountId() == seekerProfile.getUserAccountId());

     if (alreadyApplied) {
         return "redirect:/applied-jobs";
     }

        JobSeekerApply apply = new JobSeekerApply();
        apply.setUserId(seekerProfile);
        apply.setJob(job);
        apply.setApplyDate(new Date());

       
        apply.setStatus("Pending");

        jobSeekerApplyService.addNew(apply);

        return "redirect:/applied-jobs";
    }
}
