package com.example.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.JobSeekerProfile;
import com.example.demo.entity.User;

@Repository
public interface JobSeekerProfileRepository extends JpaRepository<JobSeekerProfile, Integer> {

    // Find job seeker profile by the associated user account
	Optional<JobSeekerProfile> findByUserAccountId(Integer userAccountId);
}