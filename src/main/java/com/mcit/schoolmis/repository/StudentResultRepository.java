package com.mcit.schoolmis.repository;

import com.mcit.schoolmis.domain.StudentResult;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the StudentResult entity.
 */
@SuppressWarnings("unused")
@Repository
public interface StudentResultRepository extends JpaRepository<StudentResult, Long> {}
