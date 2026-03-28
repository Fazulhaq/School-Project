package com.mcit.schoolmis.web.rest;

import com.mcit.schoolmis.domain.TeacherSubject;
import com.mcit.schoolmis.repository.TeacherSubjectRepository;
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
 * REST controller for managing {@link com.mcit.schoolmis.domain.TeacherSubject}.
 */
@RestController
@RequestMapping("/api/teacher-subjects")
@Transactional
public class TeacherSubjectResource {

    private static final Logger log = LoggerFactory.getLogger(TeacherSubjectResource.class);

    private static final String ENTITY_NAME = "teacherSubject";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TeacherSubjectRepository teacherSubjectRepository;

    public TeacherSubjectResource(TeacherSubjectRepository teacherSubjectRepository) {
        this.teacherSubjectRepository = teacherSubjectRepository;
    }

    /**
     * {@code POST  /teacher-subjects} : Create a new teacherSubject.
     *
     * @param teacherSubject the teacherSubject to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new teacherSubject, or with status {@code 400 (Bad Request)} if the teacherSubject has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<TeacherSubject> createTeacherSubject(@RequestBody TeacherSubject teacherSubject) throws URISyntaxException {
        log.debug("REST request to save TeacherSubject : {}", teacherSubject);
        if (teacherSubject.getId() != null) {
            throw new BadRequestAlertException("A new teacherSubject cannot already have an ID", ENTITY_NAME, "idexists");
        }
        teacherSubject = teacherSubjectRepository.save(teacherSubject);
        return ResponseEntity.created(new URI("/api/teacher-subjects/" + teacherSubject.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, teacherSubject.getId().toString()))
            .body(teacherSubject);
    }

    /**
     * {@code PUT  /teacher-subjects/:id} : Updates an existing teacherSubject.
     *
     * @param id the id of the teacherSubject to save.
     * @param teacherSubject the teacherSubject to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated teacherSubject,
     * or with status {@code 400 (Bad Request)} if the teacherSubject is not valid,
     * or with status {@code 500 (Internal Server Error)} if the teacherSubject couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<TeacherSubject> updateTeacherSubject(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody TeacherSubject teacherSubject
    ) throws URISyntaxException {
        log.debug("REST request to update TeacherSubject : {}, {}", id, teacherSubject);
        if (teacherSubject.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, teacherSubject.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!teacherSubjectRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        teacherSubject = teacherSubjectRepository.save(teacherSubject);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, teacherSubject.getId().toString()))
            .body(teacherSubject);
    }

    /**
     * {@code PATCH  /teacher-subjects/:id} : Partial updates given fields of an existing teacherSubject, field will ignore if it is null
     *
     * @param id the id of the teacherSubject to save.
     * @param teacherSubject the teacherSubject to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated teacherSubject,
     * or with status {@code 400 (Bad Request)} if the teacherSubject is not valid,
     * or with status {@code 404 (Not Found)} if the teacherSubject is not found,
     * or with status {@code 500 (Internal Server Error)} if the teacherSubject couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TeacherSubject> partialUpdateTeacherSubject(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody TeacherSubject teacherSubject
    ) throws URISyntaxException {
        log.debug("REST request to partial update TeacherSubject partially : {}, {}", id, teacherSubject);
        if (teacherSubject.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, teacherSubject.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!teacherSubjectRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TeacherSubject> result = teacherSubjectRepository.findById(teacherSubject.getId()).map(teacherSubjectRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, teacherSubject.getId().toString())
        );
    }

    /**
     * {@code GET  /teacher-subjects} : get all the teacherSubjects.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of teacherSubjects in body.
     */
    @GetMapping("")
    public List<TeacherSubject> getAllTeacherSubjects() {
        log.debug("REST request to get all TeacherSubjects");
        return teacherSubjectRepository.findAll();
    }

    /**
     * {@code GET  /teacher-subjects/:id} : get the "id" teacherSubject.
     *
     * @param id the id of the teacherSubject to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the teacherSubject, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<TeacherSubject> getTeacherSubject(@PathVariable("id") Long id) {
        log.debug("REST request to get TeacherSubject : {}", id);
        Optional<TeacherSubject> teacherSubject = teacherSubjectRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(teacherSubject);
    }

    /**
     * {@code DELETE  /teacher-subjects/:id} : delete the "id" teacherSubject.
     *
     * @param id the id of the teacherSubject to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTeacherSubject(@PathVariable("id") Long id) {
        log.debug("REST request to delete TeacherSubject : {}", id);
        teacherSubjectRepository.deleteById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
