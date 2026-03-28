package com.mcit.schoolmis.web.rest;

import static com.mcit.schoolmis.domain.FeeStructureAsserts.*;
import static com.mcit.schoolmis.web.rest.TestUtil.createUpdateProxyForBean;
import static com.mcit.schoolmis.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mcit.schoolmis.IntegrationTest;
import com.mcit.schoolmis.domain.FeeStructure;
import com.mcit.schoolmis.repository.FeeStructureRepository;
import jakarta.persistence.EntityManager;
import java.math.BigDecimal;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link FeeStructureResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class FeeStructureResourceIT {

    private static final BigDecimal DEFAULT_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_AMOUNT = new BigDecimal(2);

    private static final String DEFAULT_ACADEMIC_YEAR = "AAAAAAAAAA";
    private static final String UPDATED_ACADEMIC_YEAR = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/fee-structures";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private FeeStructureRepository feeStructureRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFeeStructureMockMvc;

    private FeeStructure feeStructure;

    private FeeStructure insertedFeeStructure;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FeeStructure createEntity(EntityManager em) {
        FeeStructure feeStructure = new FeeStructure().amount(DEFAULT_AMOUNT).academicYear(DEFAULT_ACADEMIC_YEAR);
        return feeStructure;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FeeStructure createUpdatedEntity(EntityManager em) {
        FeeStructure feeStructure = new FeeStructure().amount(UPDATED_AMOUNT).academicYear(UPDATED_ACADEMIC_YEAR);
        return feeStructure;
    }

    @BeforeEach
    public void initTest() {
        feeStructure = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedFeeStructure != null) {
            feeStructureRepository.delete(insertedFeeStructure);
            insertedFeeStructure = null;
        }
    }

    @Test
    @Transactional
    void createFeeStructure() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the FeeStructure
        var returnedFeeStructure = om.readValue(
            restFeeStructureMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(feeStructure)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            FeeStructure.class
        );

        // Validate the FeeStructure in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertFeeStructureUpdatableFieldsEquals(returnedFeeStructure, getPersistedFeeStructure(returnedFeeStructure));

        insertedFeeStructure = returnedFeeStructure;
    }

    @Test
    @Transactional
    void createFeeStructureWithExistingId() throws Exception {
        // Create the FeeStructure with an existing ID
        feeStructure.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFeeStructureMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(feeStructure)))
            .andExpect(status().isBadRequest());

        // Validate the FeeStructure in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllFeeStructures() throws Exception {
        // Initialize the database
        insertedFeeStructure = feeStructureRepository.saveAndFlush(feeStructure);

        // Get all the feeStructureList
        restFeeStructureMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(feeStructure.getId().intValue())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(sameNumber(DEFAULT_AMOUNT))))
            .andExpect(jsonPath("$.[*].academicYear").value(hasItem(DEFAULT_ACADEMIC_YEAR)));
    }

    @Test
    @Transactional
    void getFeeStructure() throws Exception {
        // Initialize the database
        insertedFeeStructure = feeStructureRepository.saveAndFlush(feeStructure);

        // Get the feeStructure
        restFeeStructureMockMvc
            .perform(get(ENTITY_API_URL_ID, feeStructure.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(feeStructure.getId().intValue()))
            .andExpect(jsonPath("$.amount").value(sameNumber(DEFAULT_AMOUNT)))
            .andExpect(jsonPath("$.academicYear").value(DEFAULT_ACADEMIC_YEAR));
    }

    @Test
    @Transactional
    void getNonExistingFeeStructure() throws Exception {
        // Get the feeStructure
        restFeeStructureMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingFeeStructure() throws Exception {
        // Initialize the database
        insertedFeeStructure = feeStructureRepository.saveAndFlush(feeStructure);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the feeStructure
        FeeStructure updatedFeeStructure = feeStructureRepository.findById(feeStructure.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedFeeStructure are not directly saved in db
        em.detach(updatedFeeStructure);
        updatedFeeStructure.amount(UPDATED_AMOUNT).academicYear(UPDATED_ACADEMIC_YEAR);

        restFeeStructureMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedFeeStructure.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedFeeStructure))
            )
            .andExpect(status().isOk());

        // Validate the FeeStructure in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedFeeStructureToMatchAllProperties(updatedFeeStructure);
    }

    @Test
    @Transactional
    void putNonExistingFeeStructure() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        feeStructure.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFeeStructureMockMvc
            .perform(
                put(ENTITY_API_URL_ID, feeStructure.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(feeStructure))
            )
            .andExpect(status().isBadRequest());

        // Validate the FeeStructure in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchFeeStructure() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        feeStructure.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFeeStructureMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(feeStructure))
            )
            .andExpect(status().isBadRequest());

        // Validate the FeeStructure in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFeeStructure() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        feeStructure.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFeeStructureMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(feeStructure)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the FeeStructure in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateFeeStructureWithPatch() throws Exception {
        // Initialize the database
        insertedFeeStructure = feeStructureRepository.saveAndFlush(feeStructure);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the feeStructure using partial update
        FeeStructure partialUpdatedFeeStructure = new FeeStructure();
        partialUpdatedFeeStructure.setId(feeStructure.getId());

        partialUpdatedFeeStructure.academicYear(UPDATED_ACADEMIC_YEAR);

        restFeeStructureMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFeeStructure.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedFeeStructure))
            )
            .andExpect(status().isOk());

        // Validate the FeeStructure in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertFeeStructureUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedFeeStructure, feeStructure),
            getPersistedFeeStructure(feeStructure)
        );
    }

    @Test
    @Transactional
    void fullUpdateFeeStructureWithPatch() throws Exception {
        // Initialize the database
        insertedFeeStructure = feeStructureRepository.saveAndFlush(feeStructure);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the feeStructure using partial update
        FeeStructure partialUpdatedFeeStructure = new FeeStructure();
        partialUpdatedFeeStructure.setId(feeStructure.getId());

        partialUpdatedFeeStructure.amount(UPDATED_AMOUNT).academicYear(UPDATED_ACADEMIC_YEAR);

        restFeeStructureMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFeeStructure.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedFeeStructure))
            )
            .andExpect(status().isOk());

        // Validate the FeeStructure in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertFeeStructureUpdatableFieldsEquals(partialUpdatedFeeStructure, getPersistedFeeStructure(partialUpdatedFeeStructure));
    }

    @Test
    @Transactional
    void patchNonExistingFeeStructure() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        feeStructure.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFeeStructureMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, feeStructure.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(feeStructure))
            )
            .andExpect(status().isBadRequest());

        // Validate the FeeStructure in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFeeStructure() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        feeStructure.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFeeStructureMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(feeStructure))
            )
            .andExpect(status().isBadRequest());

        // Validate the FeeStructure in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFeeStructure() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        feeStructure.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFeeStructureMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(feeStructure)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the FeeStructure in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteFeeStructure() throws Exception {
        // Initialize the database
        insertedFeeStructure = feeStructureRepository.saveAndFlush(feeStructure);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the feeStructure
        restFeeStructureMockMvc
            .perform(delete(ENTITY_API_URL_ID, feeStructure.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return feeStructureRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected FeeStructure getPersistedFeeStructure(FeeStructure feeStructure) {
        return feeStructureRepository.findById(feeStructure.getId()).orElseThrow();
    }

    protected void assertPersistedFeeStructureToMatchAllProperties(FeeStructure expectedFeeStructure) {
        assertFeeStructureAllPropertiesEquals(expectedFeeStructure, getPersistedFeeStructure(expectedFeeStructure));
    }

    protected void assertPersistedFeeStructureToMatchUpdatableProperties(FeeStructure expectedFeeStructure) {
        assertFeeStructureAllUpdatablePropertiesEquals(expectedFeeStructure, getPersistedFeeStructure(expectedFeeStructure));
    }
}
