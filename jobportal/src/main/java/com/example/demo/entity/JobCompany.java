package com.example.demo.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class JobCompany {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int Id;
	
	private String name;
	private String logo;
	public JobCompany() {
		super();
		
	}
	public JobCompany(int id, String name, String logo) {
		super();
		Id = id;
		this.name = name;
		this.logo = logo;
	}
	public int getId() {
		return Id;
	}
	public void setId(int id) {
		Id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getLogo() {
		return logo;
	}
	public void setLogo(String logo) {
		this.logo = logo;
	}
	@Override
	public String toString() {
		return "JobCompany [Id=" + Id + ", name=" + name + ", logo=" + logo + "]";
	}
	
	
	
}