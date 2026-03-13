# job-portal-springboot
Job Portal application built using Spring Boot, Thymeleaf and MySQL


## Overview

The **Job Portal Application** is a full-stack web application built using **Spring Boot, Thymeleaf, and MySQL**.  
It allows **recruiters to post and manage job listings** while **job seekers can search and apply for jobs**.

The system implements **secure authentication, role-based authorization, resume upload functionality, and efficient job browsing using pagination and sorting**.

This project demonstrates a **real-world backend architecture using Spring Boot**, including layered design, database interaction, secure access control, and proper exception handling.

---

## Features

### Recruiter
- Create and post new job listings
- View all jobs posted by the recruiter
- Delete job postings (only the recruiter who posted the job can delete it)
- View candidates who applied for a job
- Manage job postings from recruiter dashboard

### Job Seeker
- Search and browse available jobs
- Apply for jobs
- Upload and manage resume
- View personal profile information
- Track job applications

### System Features
- Role-based authentication using Spring Security
- Secure login and registration system
- Resume upload and viewing functionality
- Pagination and sorting for job listings
- Global exception handling
- Email verification during user registration
- Clean MVC architecture

---

## Technology Stack

### Backend
- Java
- Spring Boot
- Spring Security
- Spring Data JPA
- Hibernate

### Frontend
- Thymeleaf
- Bootstrap 5
- HTML5
- CSS3


### Database
- MySQL

### Tools
- Spring Tool Suite (STS)
- Maven
- Git
- GitHub

---

## Project Architecture

Client (Browser)
|
Thymeleaf Templates (View)
|
Spring Boot Controllers
|
Service Layer (Business Logic)
|
Repository Layer (JPA / Hibernate)
|
MySQL Database


## Security Features

- Authentication using Spring Security
- Role-based access control (Recruiter / Job Seeker)
- Secure job deletion authorization
- Protected recruiter dashboard
- Password encryption

---

## Future Improvements

- REST API version of the application
- Docker containerization
- Cloud deployment
- Job recommendation system
- Advanced job search filters
- Email notification enhancements

---

## Author

**AmeetReddy**

Backend Developer  
Java | Spring Boot | REST APIs


## Project Purpose

This project was developed to demonstrate **backend development skills using Spring Boot**, including authentication, database interaction, layered architecture, and real-world job portal functionality.
