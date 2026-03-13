package com.example.demo.entity;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "users")
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int userId;

	@Email(message = "Enter a valid email")
	@NotBlank(message = "Email is required")
	@Column(unique = true)
	private String email;

	@NotBlank(message = "Password is required")
	@Size(min = 8, message = "Password must be at least 8 characters")
	@Pattern(
		regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).*$",
		message = "Password must contain at least one uppercase, one lowercase and one number"
	)
	private String password;

	private boolean isActive;

	@DateTimeFormat(pattern = "dd-MM-yyyy")
	private Date registrationDate;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "userTypeId", referencedColumnName = "userTypeId")
	private UserType userTypeId;

	/* --------- Constructors --------- */

	public User() {
	}

	public User(int userId, String email, String password, boolean isActive, Date registrationDate, UserType userTypeId) {
		this.userId = userId;
		this.email = email;
		this.password = password;
		this.isActive = isActive;
		this.registrationDate = registrationDate;
		this.userTypeId = userTypeId;
	}

	/* --------- Getters & Setters --------- */

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	public Date getRegistrationDate() {
		return registrationDate;
	}

	public void setRegistrationDate(Date registrationDate) {
		this.registrationDate = registrationDate;
	}

	public UserType getUserTypeId() {
		return userTypeId;
	}

	public void setUserTypeId(UserType userTypeId) {
		this.userTypeId = userTypeId;
	}

	@Override
	public String toString() {
		return "User [userId=" + userId + ", email=" + email + ", password=" + password + ", isActive=" + isActive
				+ ", registrationDate=" + registrationDate + ", userTypeId=" + userTypeId + "]";
	}
}