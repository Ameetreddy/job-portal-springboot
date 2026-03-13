package com.example.demo.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.IRecruiterJob;
import com.example.demo.entity.JobPostActivity;

@Repository
public interface JobPostActivityRepository
        extends JpaRepository<JobPostActivity, Integer> {

    @Query(value = """
            SELECT COUNT(s.user_id) AS totalCandidates,
                   j.job_post_id,
                   j.job_title,
                   l.id AS locationId,
                   c.id AS companyId,
                   c.name AS companyName
            FROM job_post_activity j
            INNER JOIN job_location l ON j.job_location_id = l.id
            INNER JOIN job_company c ON j.job_company_id = c.id
            LEFT JOIN job_seeker_apply s 
                   ON s.job= j.job_post_id
            WHERE j.posted_by_id = :recruiter
            GROUP BY j.job_post_id, j.job_title, l.id, c.id
        """, nativeQuery = true)
    List<IRecruiterJob> getRecruiterJobs(@Param("recruiter") int recruiter);


    /* -------------------------
       JOB SEEKER SEARCH (NO DATE)
    ------------------------- */
    @Query(value = """
        SELECT j.* FROM job_post_activity j
        INNER JOIN job_location l ON j.job_location_id = l.id
        WHERE j.job_title LIKE CONCAT('%', :job, '%')
          AND (l.city LIKE CONCAT('%', :location, '%')
            OR l.state LIKE CONCAT('%', :location, '%')
            OR l.country LIKE CONCAT('%', :location, '%'))
          AND j.job_type IN (:type)
          AND j.remote IN (:remote)
        """,
        countQuery = """
        SELECT COUNT(*) FROM job_post_activity j
        INNER JOIN job_location l ON j.job_location_id = l.id
        WHERE j.job_title LIKE CONCAT('%', :job, '%')
          AND (l.city LIKE CONCAT('%', :location, '%')
            OR l.state LIKE CONCAT('%', :location, '%')
            OR l.country LIKE CONCAT('%', :location, '%'))
          AND j.job_type IN (:type)
          AND j.remote IN (:remote)
        """,
        nativeQuery = true)
    Page<JobPostActivity> searchWithoutDate(
            @Param("job") String job,
            @Param("location") String location,
            @Param("type") List<String> type,
            @Param("remote") List<String> remote,
            Pageable pageable
    );


    /* -------------------------
       JOB SEEKER SEARCH (WITH DATE)
    ------------------------- */
    @Query(value = """
        SELECT j.* FROM job_post_activity j
        INNER JOIN job_location l ON j.job_location_id = l.id
        WHERE j.job_title LIKE CONCAT('%', :job, '%')
          AND (l.city LIKE CONCAT('%', :location, '%')
            OR l.state LIKE CONCAT('%', :location, '%')
            OR l.country LIKE CONCAT('%', :location, '%'))
          AND j.job_type IN (:type)
          AND j.remote IN (:remote)
          AND j.posted_date >= :date
        """,
        countQuery = """
        SELECT COUNT(*) FROM job_post_activity j
        INNER JOIN job_location l ON j.job_location_id = l.id
        WHERE j.job_title LIKE CONCAT('%', :job, '%')
          AND (l.city LIKE CONCAT('%', :location, '%')
            OR l.state LIKE CONCAT('%', :location, '%')
            OR l.country LIKE CONCAT('%', :location, '%'))
          AND j.job_type IN (:type)
          AND j.remote IN (:remote)
          AND j.posted_date >= :date
        """,
        nativeQuery = true)
    Page<JobPostActivity> search(
            @Param("job") String job,
            @Param("location") String location,
            @Param("type") List<String> type,
            @Param("remote") List<String> remote,
            @Param("date") Date date,
            Pageable pageable
    );
}