package com.mcit.schoolmis.web.rest;

import static com.mcit.schoolmis.domain.StudentResultAsserts.*;
import static com.mcit.schoolmis.web.rest.TestUtil.createUpdateProxyForBean;
import static com.mcit.schoolmis.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mcit.schoolmis.IntegrationTest;
import com.mcit.schoolmis.domain.StudentResult;
import com.mcit.schoolmis.repository.StudentResultRepository;
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
 * Integration tests for the {@link StudentResultResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class StudentResultResourceIT {

    private static final BigDecimal DEFAULT_MARKS_OBTAINED = new BigDecimal(1);
    private static final BigDecimal UPDATED_MARKS_OBTAINED = new BigDecimal(2);

    private static final String ENTITY_API_URL = "/api/student-results";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private StudentResultRepository studentResultRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restStudentResultMockMvc;

    private StudentResult studentResult;

    private StudentResult insertedStudentResult;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static StudentResult createEntity(EntityManager em) {
        StudentResult studentResult = new StudentResult().marksObtained(DEFAULT_MARKS_OBTAINED);
        return studentResult;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static StudentResult createUpdatedEntity(EntityManager em) {
        StudentResult studentResult = new StudentResult().marksObtained(UPDATED_MARKS_OBTAINED);
        return studentResult;
    }

    @BeforeEach
    public void initTest() {
        studentResult = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedStudentResult != null) {
            studentResultRepository.delete(insertedStudentResult);
            insertedStudentResult = null;
        }
    }

    @Test
    @Transactional
    void createStudentResult() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the StudentResult
        var returnedStudentResult = om.readValue(
            restStudentResultMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(studentResult)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            StudentResult.class
        );

        // Validate the StudentResult in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertStudentResultUpdatableFieldsEquals(returnedStudentResult, getPersistedStudentResult(returnedStudentResult));

        insertedStudentResult = returnedStudentResult;
    }

    @Test
    @Transactional
    void createStudentResultWithExistingId() throws Exception {
        // Create the StudentResult with an existing ID
        studentResult.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restStudentResultMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(studentResult)))
            .andExpect(status().isBadRequest());

        // Validate the StudentResult in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllStudentResults() throws Exception {
        // Initialize the database
        insertedStudentResult = studentResultRepository.saveAndFlush(studentResult);

        // Get all the studentResultList
        restStudentResultMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(studentResult.getId().intValue())))
            .andExpect(jsonPath("$.[*].marksObtained").value(hasItem(sameNumber(DEFAULT_MARKS_OBTAINED))));
    }

    @Test
    @Transactional
    void getStudentResult() throws Exception {
        // Initialize the database
        insertedStudentResult = studentResultRepository.saveAndFlush(studentResult);

        // Get the studentResult
        restStudentResultMockMvc
            .perform(get(ENTITY_API_URL_ID, studentResult.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(studentResult.getId().intValue()))
            .andExpect(jsonPath("$.marksObtained").value(sameNumber(DEFAULT_MARKS_OBTAINED)));
    }

    @Test
    @Transactional
    void getNonExistingStudentResult() throws Exception {
        // Get the studentResult
        restStudentResultMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingStudentResult() throws Exception {
        // Initialize the database
        insertedStudentResult = studentResultRepository.saveAndFlush(studentResult);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the studentResult
        StudentResult updatedStudentResult = studentResultRepository.findById(studentResult.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedStudentResult are not directly saved in db
        em.detach(updatedStudentResult);
        updatedStudentResult.marksObtained(UPDATED_MARKS_OBTAINED);

        restStudentResultMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedStudentResult.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedStudentResult))
            )
            .andExpect(status().isOk());

        // Validate the StudentResult in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedStudentResultToMatchAllProperties(updatedStudentResult);
    }

    @Test
    @Transactional
    void putNonExistingStudentResult() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        studentResult.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStudentResultMockMvc
            .perform(
                put(ENTITY_API_URL_ID, studentResult.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(studentResult))
            )
            .andExpect(status().isBadRequest());

        // Validate the StudentResult in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchStudentResult() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        studentResult.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStudentResultMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(studentResult))
            )
            .andExpect(status().isBadRequest());

        // Validate the StudentResult in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamStudentResult() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        studentResult.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStudentResultMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(studentResult)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the StudentResult in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateStudentResultWithPatch() throws Exception {
        // Initialize the database
        insertedStudentResult = studentResultRepository.saveAndFlush(studentResult);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the studentResult using partial update
        StudentResult partialUpdatedStudentResult = new StudentResult();
        partialUpdatedStudentResult.setId(studentResult.getId());

        partialUpdatedStudentResult.marksObtained(UPDATED_MARKS_OBTAINED);

        restStudentResultMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedStudentResult.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedStudentResult))
            )
            .andExpect(status().isOk());

        // Validate the StudentResult in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertStudentResultUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedStudentResult, studentResult),
            getPersistedStudentResult(studentResult)
        );
    }

    @Test
    @Transactional
    void fullUpdateStudentResultWithPatch() throws Exception {
        // Initialize the database
        insertedStudentResult = studentResultRepository.saveAndFlush(studentResult);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the studentResult using partial update
        StudentResult partialUpdatedStudentResult = new StudentResult();
        partialUpdatedStudentResult.setId(studentResult.getId());

        partialUpdatedStudentResult.marksObtained(UPDATED_MARKS_OBTAINED);

        restStudentResultMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedStudentResult.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedStudentResult))
            )
            .andExpect(status().isOk());

        // Validate the StudentResult in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertStudentResultUpdatableFieldsEquals(partialUpdatedStudentResult, getPersistedStudentResult(partialUpdatedStudentResult));
    }

    @Test
    @Transactional
    void patchNonExistingStudentResult() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        studentResult.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStudentResultMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, studentResult.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(studentResult))
            )
            .andExpect(status().isBadRequest());

        // Validate the StudentResult in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchStudentResult() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        studentResult.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStudentResultMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(studentResult))
            )
            .andExpect(status().isBadRequest());

        // Validate the StudentResult in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamStudentResult() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        studentResult.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStudentResultMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(studentResult)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the StudentResult in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteStudentResult() throws Exception {
        // Initialize the database
        insertedStudentResult = studentResultRepository.saveAndFlush(studentResult);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the studentResult
        restStudentResultMockMvc
            .perform(delete(ENTITY_API_URL_ID, studentResult.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return studentResultRepository.count();
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

    protected StudentResult getPersistedStudentResult(StudentResult studentResult) {
        return studentResultRepository.findById(studentResult.getId()).orElseThrow();
    }

    protected void assertPersistedStudentResultToMatchAllProperties(StudentResult expectedStudentResult) {
        assertStudentResultAllPropertiesEquals(expectedStudentResult, getPersistedStudentResult(expectedStudentResult));
    }

    protected void assertPersistedStudentResultToMatchUpdatableProperties(StudentResult expectedStudentResult) {
        assertStudentResultAllUpdatablePropertiesEquals(expectedStudentResult, getPersistedStudentResult(expectedStudentResult));
    }
}
