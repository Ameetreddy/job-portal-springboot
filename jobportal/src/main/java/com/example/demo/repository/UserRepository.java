package com.example.demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.entity.User;

public interface UserRepository extends JpaRepository<User,Integer> {

	Optional<User> findByEmail(String email);
	@Query("SELECT u FROM User u WHERE u.userTypeId.userTypeName = :type")
	List<User> findByUserTypeName(@Param("type") String type);
	
}

