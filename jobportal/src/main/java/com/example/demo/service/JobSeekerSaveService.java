package com.example.demo.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.entity.JobPostActivity;

import com.example.demo.entity.JobSeekerProfile;
import com.example.demo.entity.JobSeekerSave;
import com.example.demo.repository.JobSeekerSaveRepository;

@Service
public class JobSeekerSaveService {
	private final JobSeekerSaveRepository jobSeekerSaveRepository;

	
	public JobSeekerSaveService(JobSeekerSaveRepository jobSeekerSaveRepository) {
		
		this.jobSeekerSaveRepository = jobSeekerSaveRepository;
	}

	public List<JobSeekerSave> getCandidatesJobs(JobSeekerProfile userAccountId){
		return jobSeekerSaveRepository.findByUserId(userAccountId);
	}
	public List<JobSeekerSave> getJobCandidates(JobPostActivity job){
		return jobSeekerSaveRepository.findByJob(job);
	}

	public void addNew(JobSeekerSave jobSeekerSave) {
		jobSeekerSaveRepository.save(jobSeekerSave);
		
	}
 
	
	
}
