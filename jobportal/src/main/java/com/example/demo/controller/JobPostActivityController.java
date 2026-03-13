package com.example.demo.controller;

import java.security.Principal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.entity.JobPostActivity;
import com.example.demo.entity.JobSeekerApply;
import com.example.demo.entity.JobSeekerProfile;
import com.example.demo.entity.JobSeekerSave;
import com.example.demo.entity.RecruiterJobDto;
import com.example.demo.entity.RecruiterProfile;
import com.example.demo.entity.User;
import com.example.demo.service.JobPostActivityService;
import com.example.demo.service.JobSeekerApplyService;
import com.example.demo.service.JobSeekerSaveService;
import com.example.demo.service.UserService;

import jakarta.validation.Valid;

@Controller
public class JobPostActivityController {

    private final UserService userService;
    private final JobPostActivityService jobPostActivityService;
    private final JobSeekerApplyService jobSeekerApplyService;
    private final JobSeekerSaveService jobSeekerSaveService;

    @Autowired
    public JobPostActivityController(
            UserService userService,
            JobPostActivityService jobPostActivityService,
            JobSeekerApplyService jobSeekerApplyService,
            JobSeekerSaveService jobSeekerSaveService) {

        this.userService = userService;
        this.jobPostActivityService = jobPostActivityService;
        this.jobSeekerApplyService = jobSeekerApplyService;
        this.jobSeekerSaveService = jobSeekerSaveService;
    }

    /* -------------------------
       COMMON DASHBOARD ENTRY
    ------------------------- */
    @GetMapping("/dashboard/")
    public String dashboardRedirect(Authentication authentication) {
        if (authentication.getAuthorities()
                .contains(new SimpleGrantedAuthority("ROLE_RECRUITER"))) {
            return "redirect:/dashboard/recruiter";
        }
        return "redirect:/dashboard/jobseeker";
    }

    /* -------------------------
       JOB SEEKER DASHBOARD
    ------------------------- */
    @GetMapping("/dashboard/jobseeker")
    public String jobSeekerDashboard(
            Model model,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "2") int size,
            @RequestParam(required = false) String job,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) String partTime,
            @RequestParam(required = false) String fullTime,
            @RequestParam(required = false) String freelance,
            @RequestParam(required = false) String remoteOnly,
            @RequestParam(required = false) String officeOnly,
            @RequestParam(required = false) String partialRemote,
            @RequestParam(required = false) boolean today,
            @RequestParam(required = false) boolean days7,
            @RequestParam(required = false) boolean days30) {

        LocalDate searchDate = null;

        if (days30) searchDate = LocalDate.now().minusDays(30);
        else if (days7) searchDate = LocalDate.now().minusDays(7);
        else if (today) searchDate = LocalDate.now();

        if (partTime == null && fullTime == null && freelance == null) {
            partTime = "Part-Time";
            fullTime = "Full-Time";
            freelance = "Freelance";
        }

        if (officeOnly == null && remoteOnly == null && partialRemote == null) {
            officeOnly = "Office-Only";
            remoteOnly = "Remote-Only";
            partialRemote = "Partial-Remote";
        }

        if (!StringUtils.hasText(job)) job = "";
        if (!StringUtils.hasText(location)) location = "";

        Page<JobPostActivity> jobPage =
                jobPostActivityService.search(
                        job,
                        location,
                        Arrays.asList(partTime, fullTime, freelance),
                        Arrays.asList(remoteOnly, officeOnly, partialRemote),
                        searchDate,
                        page,
                        size
                );

        List<JobPostActivity> jobPost = jobPage.getContent();

        JobSeekerProfile profile =
                (JobSeekerProfile) userService.getCurrentUserProfile();

        List<JobSeekerApply> applied =
                jobSeekerApplyService.getCandidatesJobs(profile);

        List<JobSeekerSave> saved =
                jobSeekerSaveService.getCandidatesJobs(profile);

        for (JobPostActivity jobActivity : jobPost) {
            jobActivity.setIsActive(
                    applied.stream().anyMatch(a ->
                            Objects.equals(
                                    a.getJob().getJobPostId(),
                                    jobActivity.getJobPostId())));

            jobActivity.setIsSaved(
                    saved.stream().anyMatch(s ->
                            Objects.equals(
                                    s.getJob().getJobPostId(),
                                    jobActivity.getJobPostId())));
        }

        model.addAttribute("jobPost", jobPost);
        model.addAttribute("user", profile);

        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", jobPage.getTotalPages());

        return "jobseeker-dashboard";
    }

    /* -------------------------
       RECRUITER DASHBOARD
    ------------------------- */
    @GetMapping("/dashboard/recruiter")
    public String recruiterDashboard(Model model) {

        Object profileObj = userService.getCurrentUserProfile();

        if (!(profileObj instanceof RecruiterProfile recruiter)) {
            return "redirect:/dashboard/";
        }

        List<RecruiterJobDto> recruiterJobs =
                jobPostActivityService.getRecruiterJobs(recruiter.getUserAccountId());

        model.addAttribute("jobPost", recruiterJobs);
        model.addAttribute("user", recruiter);

        return "recruiter-dashboard";
    }

    @GetMapping("/dashboard/add")
    public String addJobs(Model model) {
        model.addAttribute("jobPostActivity", new JobPostActivity());
        model.addAttribute("user", userService.getCurrentUserProfile());
        return "add-jobs";
    }

    @PostMapping("/dashboard/addNew")
    public String addNew(
            @Valid JobPostActivity jobPostActivity,
            BindingResult result,
            Model model) {

        if (result.hasErrors()) {
            model.addAttribute("jobPostActivity", jobPostActivity);
            model.addAttribute("user", userService.getCurrentUserProfile());
            return "add-jobs";
        }

        User user = userService.getCurrentUser();
        jobPostActivity.setPostedById(user);
        jobPostActivity.setPostedDate(new Date());

        jobPostActivityService.addJob(jobPostActivity);
        return "redirect:/dashboard/recruiter";
    }

    @PostMapping("/dashboard/edit/{id}")
    public String editJob(@PathVariable int id, Model model) {

        JobPostActivity jobPostActivity =
                jobPostActivityService.getOne(id);

        model.addAttribute("jobPostActivity", jobPostActivity);
        model.addAttribute("user", userService.getCurrentUserProfile());
        return "add-jobs";
    }

    /* -------------------------
       VIEW CANDIDATES FOR A JOB
    ------------------------- */
    @GetMapping("/dashboard/recruiter/job-post/{id}/applications")
    public String viewJobApplications(@PathVariable("id") int jobPostId, Model model) {

        Object profileObj = userService.getCurrentUserProfile();

        if (!(profileObj instanceof RecruiterProfile recruiter)) {
            return "redirect:/dashboard/";
        }

        JobPostActivity jobPost = jobPostActivityService.getOne(jobPostId);

        if (jobPost.getPostedById().getUserId() != recruiter.getUserAccountId()) {
            return "redirect:/dashboard/recruiter";
        }

        List<JobSeekerApply> appliedList =
                jobSeekerApplyService.getJobCandidates(jobPost);

        model.addAttribute("jobPost", jobPost);
        model.addAttribute("appliedList", appliedList);
        model.addAttribute("user", recruiter);

        return "recruiter-job-applications";
    }

    /* -------------------------
       UPDATE CANDIDATE STATUS
    ------------------------- */
    @PostMapping("/dashboard/recruiter/application/{applyId}/status")
    public String updateCandidateStatus(
            @PathVariable("applyId") int applyId,
            @RequestParam("status") String status) {

        JobSeekerApply apply =
                jobSeekerApplyService.getOne(applyId)
                        .orElseThrow(() -> new RuntimeException("Application not found"));

        apply.setStatus(status);

        jobSeekerApplyService.addNew(apply);

        return "redirect:/dashboard/recruiter/job-post/"
                + apply.getJob().getJobPostId() + "/applications";
    }

    @GetMapping("/recruiter/candidate/{id}")
    public String viewCandidateProfile(@PathVariable Integer id, Model model) {

        JobSeekerProfile profile =
                jobSeekerApplyService.getJobSeekerProfileById(id);

        Object profileObj = userService.getCurrentUserProfile();

        if (!(profileObj instanceof RecruiterProfile recruiter)) {
            return "redirect:/dashboard/";
        }

        model.addAttribute("profile", profile);
        model.addAttribute("user", recruiter);

        return "recruiter-view-candidate";
    }
    
    @PostMapping("/dashboard/recruiter/deleteJob")
    public String deleteJob(@RequestParam("id") int jobId, Principal principal) {
        String email = principal.getName();
        jobPostActivityService.deleteJobByRecruiter(jobId, email);
        return "redirect:/dashboard/recruiter";
    }
}