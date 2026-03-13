package com.example.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.RecruiterProfile;


@Repository
public interface RecruiterProfileRepository extends JpaRepository<RecruiterProfile, Integer> {

    // Find recruiter profile by the associated user account
	Optional<RecruiterProfile> findByUserAccountId(Integer userAccountId);
}

