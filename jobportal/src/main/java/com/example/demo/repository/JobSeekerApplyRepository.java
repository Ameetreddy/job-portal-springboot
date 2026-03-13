package com.example.demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.JobPostActivity;
import com.example.demo.entity.JobSeekerApply;
import com.example.demo.entity.JobSeekerProfile;

@Repository
public interface JobSeekerApplyRepository extends JpaRepository<JobSeekerApply,Integer>{
 List<JobSeekerApply> findByUserId(JobSeekerProfile userId);
 List<JobSeekerApply> findByJob(JobPostActivity job);
 Optional<JobSeekerApply> getOne(int applyId);
}