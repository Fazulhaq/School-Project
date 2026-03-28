package com.mcit.schoolmis.repository;

import com.mcit.schoolmis.domain.FeeStructure;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the FeeStructure entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FeeStructureRepository extends JpaRepository<FeeStructure, Long> {}
