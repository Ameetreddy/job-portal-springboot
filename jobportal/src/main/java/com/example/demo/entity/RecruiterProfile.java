package com.example.demo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

@Entity
@Table(name="recruiter_profile")
public class RecruiterProfile {
	
	@Id
	private int userAccountId;
	
	@OneToOne
	@JoinColumn(name="user_account_id")
	@MapsId
	private User userId;
	private String firstName;
	private String lastName;
	private String city;
	private String state;
	private String Country;
	private String company;
	
	@Column(nullable=true,length=64)
	private String profilePhoto;

	public RecruiterProfile(int userAccountId, User userid, String firstName, String lastName, String city,
			String state, String country, String company, String profilePhoto) {
		super();
		this.userAccountId = userAccountId;
		this.userId = userId;
		this.firstName = firstName;
		this.lastName = lastName;
		this.city = city;
		this.state = state;
		Country = country;
		this.company = company;
		this.profilePhoto = profilePhoto;
	}

	public int getUserAccountId() {
		return userAccountId;
	}

	public void setUserAccountId(int userAccountId) {
		this.userAccountId = userAccountId;
	}

	public User getUserid() {
		return userId;
	}

	public void setUserid(User userid) {
		this.userId = userid;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCountry() {
		return Country;
	}

	public void setCountry(String country) {
		Country = country;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getProfilePhoto() {
		return profilePhoto;
	}

	public void setProfilePhoto(String profilePhoto) {
		this.profilePhoto = profilePhoto;
	}

	public RecruiterProfile() {
		
	}

	public RecruiterProfile(User user) {
		
		this.userId=user;
	}
	
	@Transient
	public String getPhotoImagePath()
	{
		if(profilePhoto==null)
			return null;
		return "/photos/recruiter/"+userAccountId+"/"+profilePhoto;
	}

	@Override
	public String toString() {
		return "RecruiterProfile [userAccountId=" + userAccountId + ", userid=" + userId + ", firstName=" + firstName
				+ ", lastName=" + lastName + ", city=" + city + ", state=" + state + ", Country=" + Country
				+ ", company=" + company + ", profilePhoto=" + profilePhoto + "]";
	}
	

}
