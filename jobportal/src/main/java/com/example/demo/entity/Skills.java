package com.example.demo.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "skills")
public class Skills {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    @Column(name = "experience_level")
    private String experienceLevel;

    @Column(name = "years_of_experience")
    private Integer yearsOfExperience;

    // 🔑 FK COLUMN = job_seeker_profile
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_seeker_profile", nullable = false)
    private JobSeekerProfile jobSeekerProfile;

    public Skills() {}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getExperienceLevel() {
        return experienceLevel;
    }

    public void setExperienceLevel(String experienceLevel) {
        this.experienceLevel = experienceLevel;
    }

    public Integer getYearsOfExperience() {
        return yearsOfExperience;
    }

    public void setYearsOfExperience(Integer yearsOfExperience) {
        this.yearsOfExperience = yearsOfExperience;
    }

    public JobSeekerProfile getJobSeekerProfile() {
        return jobSeekerProfile;
    }

    public void setJobSeekerProfile(JobSeekerProfile jobSeekerProfile) {
        this.jobSeekerProfile = jobSeekerProfile;
    }




    @Override
    public String toString() {
        return "Skills [id=" + id +
                ", name=" + name +
                ", experienceLevel=" + experienceLevel +
                ", yearsOfExperience=" + yearsOfExperience + "]";
    }
}
