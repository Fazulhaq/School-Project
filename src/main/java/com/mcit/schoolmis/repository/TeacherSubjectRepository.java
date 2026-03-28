package com.mcit.schoolmis.repository;

import com.mcit.schoolmis.domain.TeacherSubject;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the TeacherSubject entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TeacherSubjectRepository extends JpaRepository<TeacherSubject, Long> {}
