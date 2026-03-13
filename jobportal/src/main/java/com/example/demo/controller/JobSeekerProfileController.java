package com.example.demo.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;

import com.example.demo.FileUploadUtil;
import com.example.demo.entity.JobSeekerProfile;
import com.example.demo.entity.Skills;
import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.JobSeekerProfileService;
import com.example.demo.util.FileDownloadUtil;

@Controller
@RequestMapping("/job-seeker-profile")
public class JobSeekerProfileController {

    private final JobSeekerProfileService jobSeekerProfileService;
    private final UserRepository userRepository;

   
    public JobSeekerProfileController(
            JobSeekerProfileService jobSeekerProfileService,
            UserRepository userRepository) {
        this.jobSeekerProfileService = jobSeekerProfileService;
        this.userRepository = userRepository;
    }

  
    @GetMapping("/")
    public String jobSeekerProfile(Model model) {

        JobSeekerProfile profile = new JobSeekerProfile();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            User user = userRepository.findByEmail(authentication.getName())
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));

            Optional<JobSeekerProfile> existingProfile =
                    jobSeekerProfileService.getOne(user.getUserId());

            if (existingProfile.isPresent()) {
                profile = existingProfile.get();
            }

            // Ensure at least one empty skill row for UI
            if (profile.getSkills() == null || profile.getSkills().isEmpty()) {
                List<Skills> skills = new ArrayList<>();
                skills.add(new Skills());
                profile.setSkills(skills);
            }
        }

        model.addAttribute("profile", profile);
        return "job-seeker-profile";
    }

    @PostMapping("/addNew")
    public String addNew(
            @ModelAttribute("profile") JobSeekerProfile jobSeekerProfile,
            @RequestParam("image") MultipartFile image,
            @RequestParam("pdf") MultipartFile pdf) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            User user = userRepository.findByEmail(authentication.getName())
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));

            jobSeekerProfile.setUserId(user);
            jobSeekerProfile.setUserAccountId(user.getUserId());
        }

        /* ---------- CLEAN SKILLS (DELETE REMOVED OR EMPTY ONES) ---------- */
        List<Skills> cleanedSkills = new ArrayList<>();
        if (jobSeekerProfile.getSkills() != null) {
            for (Skills skill : jobSeekerProfile.getSkills()) {
                if (skill.getName() != null && !skill.getName().trim().isEmpty()) {
                    skill.setJobSeekerProfile(jobSeekerProfile);
                    cleanedSkills.add(skill);
                }
            }
        }
        jobSeekerProfile.setSkills(cleanedSkills);

       
        try {
            
            if (image != null && !image.isEmpty()) {
                String imageName = StringUtils.cleanPath(Objects.requireNonNull(image.getOriginalFilename()));
                jobSeekerProfile.setProfilePhoto(imageName);
                String uploadDir = "photos/candidate/" + jobSeekerProfile.getUserAccountId();
                FileUploadUtil.savefile(uploadDir, imageName, image);
            }

           
            if (pdf != null && !pdf.isEmpty()) {
                String resumeName = StringUtils.cleanPath(Objects.requireNonNull(pdf.getOriginalFilename()));
                jobSeekerProfile.setResume(resumeName);
                String uploadDir = "photos/candidate/" + jobSeekerProfile.getUserAccountId();
                FileUploadUtil.savefile(uploadDir, resumeName, pdf);
            }
        } catch (IOException e) {
            throw new RuntimeException("File upload failed", e);
        }

  
        jobSeekerProfileService.addNew(jobSeekerProfile);

        return "redirect:/dashboard/";
    }

   
    @GetMapping("/{id}")
    public String candidateProfile(@PathVariable("id") int id, Model model) {

        JobSeekerProfile profile = jobSeekerProfileService.getOne(id)
                .orElseThrow(() -> new RuntimeException("Profile not found"));

        model.addAttribute("profile", profile);
        return "job-seeker-profile";
    }

  
    @GetMapping("/downloadResume")
    public ResponseEntity<?> downloadResume(
            @RequestParam("fileName") String fileName,
            @RequestParam("userID") String userID) {

        FileDownloadUtil downloadUtil = new FileDownloadUtil();
        Resource resource;

        try {
            resource = downloadUtil.getFileAsResource(
                    "photos/candidate/" + userID, fileName);
        } catch (IOException e) {
            return ResponseEntity.badRequest().build();
        }

        if (resource == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("File not found");
        }

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }
}
