package com.example.demo.service;

import java.util.List;
import java.util.Optional;


import org.springframework.stereotype.Service;

import com.example.demo.entity.JobPostActivity;
import com.example.demo.entity.JobSeekerApply;
import com.example.demo.entity.JobSeekerProfile;
import com.example.demo.repository.JobSeekerApplyRepository;
import com.example.demo.repository.JobSeekerProfileRepository;

@Service
public class JobSeekerApplyService {

    private final JobSeekerApplyRepository jobSeekerApplyRepository;
    private final JobSeekerProfileRepository jobSeekerProfileRepository; // NEW

   
    public JobSeekerApplyService(JobSeekerApplyRepository jobSeekerApplyRepository,
                                 JobSeekerProfileRepository jobSeekerProfileRepository) {
        this.jobSeekerApplyRepository = jobSeekerApplyRepository;
        this.jobSeekerProfileRepository = jobSeekerProfileRepository; // NEW
    }

    public List<JobSeekerApply> getCandidatesJobs(JobSeekerProfile userAccountId){
        return jobSeekerApplyRepository.findByUserId(userAccountId);
    }

    public List<JobSeekerApply> getJobCandidates(JobPostActivity job){
        return jobSeekerApplyRepository.findByJob(job);
    }

    public void addNew(JobSeekerApply jobSeekerApply) {
        jobSeekerApplyRepository.save(jobSeekerApply);
    }
    
    public Optional<JobSeekerApply> getOne(int applyId) {
        return jobSeekerApplyRepository.findById(applyId);
    }

    // ✅ NEW: Get JobSeekerProfile by ID
    public JobSeekerProfile getJobSeekerProfileById(Integer id) {
        return jobSeekerProfileRepository.findById(id).orElse(null);
    }
}
