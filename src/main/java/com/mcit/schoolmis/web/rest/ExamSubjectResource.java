package com.mcit.schoolmis.web.rest;

import com.mcit.schoolmis.domain.ExamSubject;
import com.mcit.schoolmis.repository.ExamSubjectRepository;
import com.mcit.schoolmis.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.mcit.schoolmis.domain.ExamSubject}.
 */
@RestController
@RequestMapping("/api/exam-subjects")
@Transactional
public class ExamSubjectResource {

    private static final Logger log = LoggerFactory.getLogger(ExamSubjectResource.class);

    private static final String ENTITY_NAME = "examSubject";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ExamSubjectRepository examSubjectRepository;

    public ExamSubjectResource(ExamSubjectRepository examSubjectRepository) {
        this.examSubjectRepository = examSubjectRepository;
    }

    /**
     * {@code POST  /exam-subjects} : Create a new examSubject.
     *
     * @param examSubject the examSubject to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new examSubject, or with status {@code 400 (Bad Request)} if the examSubject has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ExamSubject> createExamSubject(@RequestBody ExamSubject examSubject) throws URISyntaxException {
        log.debug("REST request to save ExamSubject : {}", examSubject);
        if (examSubject.getId() != null) {
            throw new BadRequestAlertException("A new examSubject cannot already have an ID", ENTITY_NAME, "idexists");
        }
        examSubject = examSubjectRepository.save(examSubject);
        return ResponseEntity.created(new URI("/api/exam-subjects/" + examSubject.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, examSubject.getId().toString()))
            .body(examSubject);
    }

    /**
     * {@code PUT  /exam-subjects/:id} : Updates an existing examSubject.
     *
     * @param id the id of the examSubject to save.
     * @param examSubject the examSubject to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated examSubject,
     * or with status {@code 400 (Bad Request)} if the examSubject is not valid,
     * or with status {@code 500 (Internal Server Error)} if the examSubject couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ExamSubject> updateExamSubject(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ExamSubject examSubject
    ) throws URISyntaxException {
        log.debug("REST request to update ExamSubject : {}, {}", id, examSubject);
        if (examSubject.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, examSubject.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!examSubjectRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        examSubject = examSubjectRepository.save(examSubject);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, examSubject.getId().toString()))
            .body(examSubject);
    }

    /**
     * {@code PATCH  /exam-subjects/:id} : Partial updates given fields of an existing examSubject, field will ignore if it is null
     *
     * @param id the id of the examSubject to save.
     * @param examSubject the examSubject to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated examSubject,
     * or with status {@code 400 (Bad Request)} if the examSubject is not valid,
     * or with status {@code 404 (Not Found)} if the examSubject is not found,
     * or with status {@code 500 (Internal Server Error)} if the examSubject couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ExamSubject> partialUpdateExamSubject(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ExamSubject examSubject
    ) throws URISyntaxException {
        log.debug("REST request to partial update ExamSubject partially : {}, {}", id, examSubject);
        if (examSubject.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, examSubject.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!examSubjectRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ExamSubject> result = examSubjectRepository
            .findById(examSubject.getId())
            .map(existingExamSubject -> {
                if (examSubject.getMaxMarks() != null) {
                    existingExamSubject.setMaxMarks(examSubject.getMaxMarks());
                }

                return existingExamSubject;
            })
            .map(examSubjectRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, examSubject.getId().toString())
        );
    }

    /**
     * {@code GET  /exam-subjects} : get all the examSubjects.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of examSubjects in body.
     */
    @GetMapping("")
    public List<ExamSubject> getAllExamSubjects() {
        log.debug("REST request to get all ExamSubjects");
        return examSubjectRepository.findAll();
    }

    /**
     * {@code GET  /exam-subjects/:id} : get the "id" examSubject.
     *
     * @param id the id of the examSubject to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the examSubject, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ExamSubject> getExamSubject(@PathVariable("id") Long id) {
        log.debug("REST request to get ExamSubject : {}", id);
        Optional<ExamSubject> examSubject = examSubjectRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(examSubject);
    }

    /**
     * {@code DELETE  /exam-subjects/:id} : delete the "id" examSubject.
     *
     * @param id the id of the examSubject to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExamSubject(@PathVariable("id") Long id) {
        log.debug("REST request to delete ExamSubject : {}", id);
        examSubjectRepository.deleteById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
