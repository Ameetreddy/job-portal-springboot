package com.example.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.PendingUser;

@Repository
public interface PendingUserRepository extends JpaRepository<PendingUser, Integer> {

    Optional<PendingUser> findByEmail(String email);

    Optional<PendingUser> findByToken(String token);
}