package com.example.demo.service;

import java.util.List;


import org.springframework.stereotype.Service;

import com.example.demo.entity.UserType;
import com.example.demo.repository.UserTypeRepository;

@Service
public class UserTypeService {
 
	private final UserTypeRepository userTypeRepository;
	

	public UserTypeService(UserTypeRepository userTypeRepository) {
		
		this.userTypeRepository = userTypeRepository;
	}
	
	public List<UserType> getAll()
	{
		return userTypeRepository.findAll();
	}
	
	
}
