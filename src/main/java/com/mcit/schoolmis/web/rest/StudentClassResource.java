package com.mcit.schoolmis.web.rest;

import com.mcit.schoolmis.domain.StudentClass;
import com.mcit.schoolmis.repository.StudentClassRepository;
import com.mcit.schoolmis.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
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
 * REST controller for managing {@link com.mcit.schoolmis.domain.StudentClass}.
 */
@RestController
@RequestMapping("/api/student-classes")
@Transactional
public class StudentClassResource {

    private static final Logger log = LoggerFactory.getLogger(StudentClassResource.class);

    private static final String ENTITY_NAME = "studentClass";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final StudentClassRepository studentClassRepository;

    public StudentClassResource(StudentClassRepository studentClassRepository) {
        this.studentClassRepository = studentClassRepository;
    }

    /**
     * {@code POST  /student-classes} : Create a new studentClass.
     *
     * @param studentClass the studentClass to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new studentClass, or with status {@code 400 (Bad Request)} if the studentClass has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<StudentClass> createStudentClass(@Valid @RequestBody StudentClass studentClass) throws URISyntaxException {
        log.debug("REST request to save StudentClass : {}", studentClass);
        if (studentClass.getId() != null) {
            throw new BadRequestAlertException("A new studentClass cannot already have an ID", ENTITY_NAME, "idexists");
        }
        studentClass = studentClassRepository.save(studentClass);
        return ResponseEntity.created(new URI("/api/student-classes/" + studentClass.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, studentClass.getId().toString()))
            .body(studentClass);
    }

    /**
     * {@code PUT  /student-classes/:id} : Updates an existing studentClass.
     *
     * @param id the id of the studentClass to save.
     * @param studentClass the studentClass to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated studentClass,
     * or with status {@code 400 (Bad Request)} if the studentClass is not valid,
     * or with status {@code 500 (Internal Server Error)} if the studentClass couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<StudentClass> updateStudentClass(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody StudentClass studentClass
    ) throws URISyntaxException {
        log.debug("REST request to update StudentClass : {}, {}", id, studentClass);
        if (studentClass.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, studentClass.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!studentClassRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        studentClass = studentClassRepository.save(studentClass);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, studentClass.getId().toString()))
            .body(studentClass);
    }

    /**
     * {@code PATCH  /student-classes/:id} : Partial updates given fields of an existing studentClass, field will ignore if it is null
     *
     * @param id the id of the studentClass to save.
     * @param studentClass the studentClass to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated studentClass,
     * or with status {@code 400 (Bad Request)} if the studentClass is not valid,
     * or with status {@code 404 (Not Found)} if the studentClass is not found,
     * or with status {@code 500 (Internal Server Error)} if the studentClass couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<StudentClass> partialUpdateStudentClass(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody StudentClass studentClass
    ) throws URISyntaxException {
        log.debug("REST request to partial update StudentClass partially : {}, {}", id, studentClass);
        if (studentClass.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, studentClass.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!studentClassRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<StudentClass> result = studentClassRepository
            .findById(studentClass.getId())
            .map(existingStudentClass -> {
                if (studentClass.getName() != null) {
                    existingStudentClass.setName(studentClass.getName());
                }

                return existingStudentClass;
            })
            .map(studentClassRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, studentClass.getId().toString())
        );
    }

    /**
     * {@code GET  /student-classes} : get all the studentClasses.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of studentClasses in body.
     */
    @GetMapping("")
    public List<StudentClass> getAllStudentClasses() {
        log.debug("REST request to get all StudentClasses");
        return studentClassRepository.findAll();
    }

    /**
     * {@code GET  /student-classes/:id} : get the "id" studentClass.
     *
     * @param id the id of the studentClass to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the studentClass, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<StudentClass> getStudentClass(@PathVariable("id") Long id) {
        log.debug("REST request to get StudentClass : {}", id);
        Optional<StudentClass> studentClass = studentClassRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(studentClass);
    }

    /**
     * {@code DELETE  /student-classes/:id} : delete the "id" studentClass.
     *
     * @param id the id of the studentClass to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudentClass(@PathVariable("id") Long id) {
        log.debug("REST request to delete StudentClass : {}", id);
        studentClassRepository.deleteById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
