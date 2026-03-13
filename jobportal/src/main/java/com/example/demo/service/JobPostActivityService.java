package com.example.demo.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.demo.entity.IRecruiterJob;
import com.example.demo.entity.JobCompany;
import com.example.demo.entity.JobLocation;
import com.example.demo.entity.JobPostActivity;
import com.example.demo.entity.RecruiterJobDto;
import com.example.demo.entity.User;
import com.example.demo.repository.JobPostActivityRepository;
import com.example.demo.repository.UserRepository;

@Service
public class JobPostActivityService {

    private final JobPostActivityRepository jobPostActivityRepository;

    private UserService userService;

    private EmailService emailService;
    
    private UserRepository userRepository;

   

	public JobPostActivityService(JobPostActivityRepository jobPostActivityRepository, UserService userService,
			EmailService emailService, UserRepository userRepository) {
		super();
		this.jobPostActivityRepository = jobPostActivityRepository;
		this.userService = userService;
		this.emailService = emailService;
		this.userRepository = userRepository;
	}

	public JobPostActivity addJob(JobPostActivity job) {

        job.setPostedDate(new Date());

        JobPostActivity savedJob = jobPostActivityRepository.save(job);

        List<User> jobSeekers = userService.getUsersByType("Job Seeker");

        for (User user : jobSeekers) {

            String subject = "New Job Posted: " + savedJob.getJobTitle();

            String body = "Hello,\n\n"
                    + "A new job has been posted.\n\n"
                    + "Job Title: " + savedJob.getJobTitle() + "\n"
                    + "Company: " + savedJob.getJobCompanyId().getName() + "\n"
                    + "Location: " + savedJob.getJobLocationId().getCity() + "\n\n"
                    + "Login to apply.\n\n"
                    + "Regards,\nJob Portal Team";

            emailService.sendSimpleMail(user.getEmail(), subject, body);
        }

        return savedJob;
    }

    public List<RecruiterJobDto> getRecruiterJobs(int recruiter) {

        List<IRecruiterJob> recruiterJobs =
                jobPostActivityRepository.getRecruiterJobs(recruiter);

        List<RecruiterJobDto> result = new ArrayList<>();

        for (IRecruiterJob rec : recruiterJobs) {
            JobLocation loc = new JobLocation(
                    rec.getLocationId(),
                    rec.getCity(),
                    rec.getState(),
                    rec.getCountry());

            JobCompany comp = new JobCompany(
                    rec.getCompanyId(),
                    rec.getName(),
                    "");

            result.add(new RecruiterJobDto(
                    rec.getTotalCandidates(),
                    rec.getJob_post_id(),
                    rec.getJob_title(),
                    loc,
                    comp));
        }

        return result;
    }

    public JobPostActivity getOne(int id) {
        return jobPostActivityRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Job not found"));
    }

    public List<JobPostActivity> getAll() {
        return jobPostActivityRepository.findAll();
    }

    /* -------------------------
       SEARCH WITH PAGINATION
    ------------------------- */
    public Page<JobPostActivity> search(
            String job,
            String location,
            List<String> type,
            List<String> remote,
            LocalDate searchDate,
            int page,
            int size) {

        type = type.stream().filter(Objects::nonNull).toList();
        remote = remote.stream().filter(Objects::nonNull).toList();

        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by("posted_date").descending()
        );

        if (searchDate == null) {
            return jobPostActivityRepository
                    .searchWithoutDate(job, location, type, remote, pageable);
        }

        Date date = java.sql.Date.valueOf(searchDate);

        return jobPostActivityRepository
                .search(job, location, type, remote, date, pageable);
    }
    
    
    public void deleteJobByRecruiter(int jobId, String email) {

        // Get the user by email
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Get the job by ID
        JobPostActivity job = jobPostActivityRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found"));

        // Check ownership
        if (job.getPostedById().getUserId() != user.getUserId()) {
            throw new RuntimeException("You are not authorized to delete this job");
        }

        // Delete the job
        jobPostActivityRepository.deleteById(jobId);
    }
}