package com.mcit.schoolmis.web.rest;

import com.mcit.schoolmis.domain.FeeStructure;
import com.mcit.schoolmis.repository.FeeStructureRepository;
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
 * REST controller for managing {@link com.mcit.schoolmis.domain.FeeStructure}.
 */
@RestController
@RequestMapping("/api/fee-structures")
@Transactional
public class FeeStructureResource {

    private static final Logger log = LoggerFactory.getLogger(FeeStructureResource.class);

    private static final String ENTITY_NAME = "feeStructure";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FeeStructureRepository feeStructureRepository;

    public FeeStructureResource(FeeStructureRepository feeStructureRepository) {
        this.feeStructureRepository = feeStructureRepository;
    }

    /**
     * {@code POST  /fee-structures} : Create a new feeStructure.
     *
     * @param feeStructure the feeStructure to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new feeStructure, or with status {@code 400 (Bad Request)} if the feeStructure has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<FeeStructure> createFeeStructure(@RequestBody FeeStructure feeStructure) throws URISyntaxException {
        log.debug("REST request to save FeeStructure : {}", feeStructure);
        if (feeStructure.getId() != null) {
            throw new BadRequestAlertException("A new feeStructure cannot already have an ID", ENTITY_NAME, "idexists");
        }
        feeStructure = feeStructureRepository.save(feeStructure);
        return ResponseEntity.created(new URI("/api/fee-structures/" + feeStructure.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, feeStructure.getId().toString()))
            .body(feeStructure);
    }

    /**
     * {@code PUT  /fee-structures/:id} : Updates an existing feeStructure.
     *
     * @param id the id of the feeStructure to save.
     * @param feeStructure the feeStructure to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated feeStructure,
     * or with status {@code 400 (Bad Request)} if the feeStructure is not valid,
     * or with status {@code 500 (Internal Server Error)} if the feeStructure couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<FeeStructure> updateFeeStructure(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody FeeStructure feeStructure
    ) throws URISyntaxException {
        log.debug("REST request to update FeeStructure : {}, {}", id, feeStructure);
        if (feeStructure.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, feeStructure.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!feeStructureRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        feeStructure = feeStructureRepository.save(feeStructure);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, feeStructure.getId().toString()))
            .body(feeStructure);
    }

    /**
     * {@code PATCH  /fee-structures/:id} : Partial updates given fields of an existing feeStructure, field will ignore if it is null
     *
     * @param id the id of the feeStructure to save.
     * @param feeStructure the feeStructure to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated feeStructure,
     * or with status {@code 400 (Bad Request)} if the feeStructure is not valid,
     * or with status {@code 404 (Not Found)} if the feeStructure is not found,
     * or with status {@code 500 (Internal Server Error)} if the feeStructure couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<FeeStructure> partialUpdateFeeStructure(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody FeeStructure feeStructure
    ) throws URISyntaxException {
        log.debug("REST request to partial update FeeStructure partially : {}, {}", id, feeStructure);
        if (feeStructure.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, feeStructure.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!feeStructureRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<FeeStructure> result = feeStructureRepository
            .findById(feeStructure.getId())
            .map(existingFeeStructure -> {
                if (feeStructure.getAmount() != null) {
                    existingFeeStructure.setAmount(feeStructure.getAmount());
                }
                if (feeStructure.getAcademicYear() != null) {
                    existingFeeStructure.setAcademicYear(feeStructure.getAcademicYear());
                }

                return existingFeeStructure;
            })
            .map(feeStructureRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, feeStructure.getId().toString())
        );
    }

    /**
     * {@code GET  /fee-structures} : get all the feeStructures.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of feeStructures in body.
     */
    @GetMapping("")
    public List<FeeStructure> getAllFeeStructures() {
        log.debug("REST request to get all FeeStructures");
        return feeStructureRepository.findAll();
    }

    /**
     * {@code GET  /fee-structures/:id} : get the "id" feeStructure.
     *
     * @param id the id of the feeStructure to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the feeStructure, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<FeeStructure> getFeeStructure(@PathVariable("id") Long id) {
        log.debug("REST request to get FeeStructure : {}", id);
        Optional<FeeStructure> feeStructure = feeStructureRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(feeStructure);
    }

    /**
     * {@code DELETE  /fee-structures/:id} : delete the "id" feeStructure.
     *
     * @param id the id of the feeStructure to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFeeStructure(@PathVariable("id") Long id) {
        log.debug("REST request to delete FeeStructure : {}", id);
        feeStructureRepository.deleteById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
