package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.UserType;

public interface UserTypeRepository extends JpaRepository<UserType,Integer> {

}