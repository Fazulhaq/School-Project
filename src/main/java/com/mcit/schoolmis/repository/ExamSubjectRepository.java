package com.mcit.schoolmis.repository;

import com.mcit.schoolmis.domain.ExamSubject;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ExamSubject entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ExamSubjectRepository extends JpaRepository<ExamSubject, Long> {}
