package com.mcit.schoolmis.web.rest;

import com.mcit.schoolmis.domain.Timetable;
import com.mcit.schoolmis.repository.TimetableRepository;
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
 * REST controller for managing {@link com.mcit.schoolmis.domain.Timetable}.
 */
@RestController
@RequestMapping("/api/timetables")
@Transactional
public class TimetableResource {

    private static final Logger log = LoggerFactory.getLogger(TimetableResource.class);

    private static final String ENTITY_NAME = "timetable";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TimetableRepository timetableRepository;

    public TimetableResource(TimetableRepository timetableRepository) {
        this.timetableRepository = timetableRepository;
    }

    /**
     * {@code POST  /timetables} : Create a new timetable.
     *
     * @param timetable the timetable to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new timetable, or with status {@code 400 (Bad Request)} if the timetable has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<Timetable> createTimetable(@RequestBody Timetable timetable) throws URISyntaxException {
        log.debug("REST request to save Timetable : {}", timetable);
        if (timetable.getId() != null) {
            throw new BadRequestAlertException("A new timetable cannot already have an ID", ENTITY_NAME, "idexists");
        }
        timetable = timetableRepository.save(timetable);
        return ResponseEntity.created(new URI("/api/timetables/" + timetable.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, timetable.getId().toString()))
            .body(timetable);
    }

    /**
     * {@code PUT  /timetables/:id} : Updates an existing timetable.
     *
     * @param id the id of the timetable to save.
     * @param timetable the timetable to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated timetable,
     * or with status {@code 400 (Bad Request)} if the timetable is not valid,
     * or with status {@code 500 (Internal Server Error)} if the timetable couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Timetable> updateTimetable(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Timetable timetable
    ) throws URISyntaxException {
        log.debug("REST request to update Timetable : {}, {}", id, timetable);
        if (timetable.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, timetable.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!timetableRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        timetable = timetableRepository.save(timetable);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, timetable.getId().toString()))
            .body(timetable);
    }

    /**
     * {@code PATCH  /timetables/:id} : Partial updates given fields of an existing timetable, field will ignore if it is null
     *
     * @param id the id of the timetable to save.
     * @param timetable the timetable to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated timetable,
     * or with status {@code 400 (Bad Request)} if the timetable is not valid,
     * or with status {@code 404 (Not Found)} if the timetable is not found,
     * or with status {@code 500 (Internal Server Error)} if the timetable couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Timetable> partialUpdateTimetable(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Timetable timetable
    ) throws URISyntaxException {
        log.debug("REST request to partial update Timetable partially : {}, {}", id, timetable);
        if (timetable.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, timetable.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!timetableRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Timetable> result = timetableRepository
            .findById(timetable.getId())
            .map(existingTimetable -> {
                if (timetable.getDayOfWeek() != null) {
                    existingTimetable.setDayOfWeek(timetable.getDayOfWeek());
                }
                if (timetable.getStartTime() != null) {
                    existingTimetable.setStartTime(timetable.getStartTime());
                }
                if (timetable.getEndTime() != null) {
                    existingTimetable.setEndTime(timetable.getEndTime());
                }

                return existingTimetable;
            })
            .map(timetableRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, timetable.getId().toString())
        );
    }

    /**
     * {@code GET  /timetables} : get all the timetables.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of timetables in body.
     */
    @GetMapping("")
    public List<Timetable> getAllTimetables() {
        log.debug("REST request to get all Timetables");
        return timetableRepository.findAll();
    }

    /**
     * {@code GET  /timetables/:id} : get the "id" timetable.
     *
     * @param id the id of the timetable to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the timetable, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Timetable> getTimetable(@PathVariable("id") Long id) {
        log.debug("REST request to get Timetable : {}", id);
        Optional<Timetable> timetable = timetableRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(timetable);
    }

    /**
     * {@code DELETE  /timetables/:id} : delete the "id" timetable.
     *
     * @param id the id of the timetable to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTimetable(@PathVariable("id") Long id) {
        log.debug("REST request to delete Timetable : {}", id);
        timetableRepository.deleteById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
