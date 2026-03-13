package com.example.demo.controller;

import java.util.Objects;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.FileUploadUtil;
import com.example.demo.entity.RecruiterProfile;
import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.JobPostActivityService;
import com.example.demo.service.RecruiterProfileService;

@Controller
@RequestMapping("/recruiter-profile")
public class RecruiterProfileController {

    private final UserRepository userRepository;
    private final RecruiterProfileService recruiterProfileService;
    private final JobPostActivityService jobPostActivityService;

    public RecruiterProfileController(
            UserRepository userRepository,
            RecruiterProfileService recruiterProfileService,
            JobPostActivityService jobPostActivityService) {

        this.userRepository = userRepository;
        this.recruiterProfileService = recruiterProfileService;
        this.jobPostActivityService = jobPostActivityService;
    }

   
    @GetMapping("/")
    public String recruiterProfile(Model model) {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        if (!(authentication instanceof AnonymousAuthenticationToken)) {

            String currentUsername = authentication.getName();

            User user = userRepository.findByEmail(currentUsername)
                    .orElseThrow(() ->
                            new UsernameNotFoundException("User not found"));

            recruiterProfileService.getOne(user.getUserId())
                    .ifPresent(profile ->
                            model.addAttribute("profile", profile));
        }

        return "recruiter_profile";
    }

    
    @PostMapping("/addNew")
    public String addNew(RecruiterProfile recruiterProfile,
                         @RequestParam("image") MultipartFile multipartFile,
                         Model model) {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        if (!(authentication instanceof AnonymousAuthenticationToken)) {

            String currentUsername = authentication.getName();
            User user = userRepository.findByEmail(currentUsername)
                    .orElseThrow(() ->
                            new UsernameNotFoundException("User not found"));

            recruiterProfile.setUserid(user);
            recruiterProfile.setUserAccountId(user.getUserId());
        }

        model.addAttribute("profile", recruiterProfile);

        String fileName = "";

        if (!multipartFile.getOriginalFilename().equals("")) {
            fileName = StringUtils.cleanPath(
                    Objects.requireNonNull(multipartFile.getOriginalFilename()));
            recruiterProfile.setProfilePhoto(fileName);
        }

        RecruiterProfile savedUser =
                recruiterProfileService.addNew(recruiterProfile);

        String uploadDir = "photos/recruiter/" + savedUser.getUserAccountId();

        try {
            FileUploadUtil.savefile(uploadDir, fileName, multipartFile);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return "redirect:/dashboard/";
    }

   
    @GetMapping("/dashboard/recruiter")
    public String recruiterDashboard(Model model) {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UsernameNotFoundException("User not found"));

    
        RecruiterProfile recruiterProfile =
                recruiterProfileService.getOne(user.getUserId()).orElse(null);

        
        model.addAttribute("username", email);
        model.addAttribute("profilePhoto",
                recruiterProfile != null ? recruiterProfile.getPhotoImagePath() : null);

      
        model.addAttribute("user", recruiterProfile);

       
        model.addAttribute(
                "jobPost",
                jobPostActivityService.getRecruiterJobs(user.getUserId()));

        return "recruiter_dashboard";
    }
}
