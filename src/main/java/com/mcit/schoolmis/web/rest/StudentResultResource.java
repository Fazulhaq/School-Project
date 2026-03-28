package com.mcit.schoolmis.web.rest;

import com.mcit.schoolmis.domain.StudentResult;
import com.mcit.schoolmis.repository.StudentResultRepository;
import com.mcit.schoolmis.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.mcit.schoolmis.domain.StudentResult}.
 */
@RestController
@RequestMapping("/api/student-results")
@Transactional
public class StudentResultResource {

    private static final Logger log = LoggerFactory.getLogger(StudentResultResource.class);

    private static final String ENTITY_NAME = "studentResult";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final StudentResultRepository studentResultRepository;

    public StudentResultResource(StudentResultRepository studentResultRepository) {
        this.studentResultRepository = studentResultRepository;
    }

    /**
     * {@code POST  /student-results} : Create a new studentResult.
     *
     * @param studentResult the studentResult to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new studentResult, or with status {@code 400 (Bad Request)} if the studentResult has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<StudentResult> createStudentResult(@RequestBody StudentResult studentResult) throws URISyntaxException {
        log.debug("REST request to save StudentResult : {}", studentResult);
        if (studentResult.getId() != null) {
            throw new BadRequestAlertException("A new studentResult cannot already have an ID", ENTITY_NAME, "idexists");
        }
        studentResult = studentResultRepository.save(studentResult);
        return ResponseEntity.created(new URI("/api/student-results/" + studentResult.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, studentResult.getId().toString()))
            .body(studentResult);
    }

    /**
     * {@code PUT  /student-results/:id} : Updates an existing studentResult.
     *
     * @param id the id of the studentResult to save.
     * @param studentResult the studentResult to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated studentResult,
     * or with status {@code 400 (Bad Request)} if the studentResult is not valid,
     * or with status {@code 500 (Internal Server Error)} if the studentResult couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<StudentResult> updateStudentResult(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody StudentResult studentResult
    ) throws URISyntaxException {
        log.debug("REST request to update StudentResult : {}, {}", id, studentResult);
        if (studentResult.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, studentResult.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!studentResultRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        studentResult = studentResultRepository.save(studentResult);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, studentResult.getId().toString()))
            .body(studentResult);
    }

    /**
     * {@code PATCH  /student-results/:id} : Partial updates given fields of an existing studentResult, field will ignore if it is null
     *
     * @param id the id of the studentResult to save.
     * @param studentResult the studentResult to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated studentResult,
     * or with status {@code 400 (Bad Request)} if the studentResult is not valid,
     * or with status {@code 404 (Not Found)} if the studentResult is not found,
     * or with status {@code 500 (Internal Server Error)} if the studentResult couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<StudentResult> partialUpdateStudentResult(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody StudentResult studentResult
    ) throws URISyntaxException {
        log.debug("REST request to partial update StudentResult partially : {}, {}", id, studentResult);
        if (studentResult.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, studentResult.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!studentResultRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<StudentResult> result = studentResultRepository
            .findById(studentResult.getId())
            .map(existingStudentResult -> {
                if (studentResult.getMarksObtained() != null) {
                    existingStudentResult.setMarksObtained(studentResult.getMarksObtained());
                }

                return existingStudentResult;
            })
            .map(studentResultRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, studentResult.getId().toString())
        );
    }

    /**
     * {@code GET  /student-results} : get all the studentResults.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of studentResults in body.
     */
    @GetMapping("")
    public ResponseEntity<List<StudentResult>> getAllStudentResults(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of StudentResults");
        Page<StudentResult> page = studentResultRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /student-results/:id} : get the "id" studentResult.
     *
     * @param id the id of the studentResult to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the studentResult, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<StudentResult> getStudentResult(@PathVariable("id") Long id) {
        log.debug("REST request to get StudentResult : {}", id);
        Optional<StudentResult> studentResult = studentResultRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(studentResult);
    }

    /**
     * {@code DELETE  /student-results/:id} : delete the "id" studentResult.
     *
     * @param id the id of the studentResult to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudentResult(@PathVariable("id") Long id) {
        log.debug("REST request to delete StudentResult : {}", id);
        studentResultRepository.deleteById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
