package com.mcit.schoolmis.web.rest;

import static com.mcit.schoolmis.domain.ExamSubjectAsserts.*;
import static com.mcit.schoolmis.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mcit.schoolmis.IntegrationTest;
import com.mcit.schoolmis.domain.ExamSubject;
import com.mcit.schoolmis.repository.ExamSubjectRepository;
import jakarta.persistence.EntityManager;
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
 * Integration tests for the {@link ExamSubjectResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ExamSubjectResourceIT {

    private static final Integer DEFAULT_MAX_MARKS = 1;
    private static final Integer UPDATED_MAX_MARKS = 2;

    private static final String ENTITY_API_URL = "/api/exam-subjects";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ExamSubjectRepository examSubjectRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restExamSubjectMockMvc;

    private ExamSubject examSubject;

    private ExamSubject insertedExamSubject;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ExamSubject createEntity(EntityManager em) {
        ExamSubject examSubject = new ExamSubject().maxMarks(DEFAULT_MAX_MARKS);
        return examSubject;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ExamSubject createUpdatedEntity(EntityManager em) {
        ExamSubject examSubject = new ExamSubject().maxMarks(UPDATED_MAX_MARKS);
        return examSubject;
    }

    @BeforeEach
    public void initTest() {
        examSubject = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedExamSubject != null) {
            examSubjectRepository.delete(insertedExamSubject);
            insertedExamSubject = null;
        }
    }

    @Test
    @Transactional
    void createExamSubject() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ExamSubject
        var returnedExamSubject = om.readValue(
            restExamSubjectMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(examSubject)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ExamSubject.class
        );

        // Validate the ExamSubject in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertExamSubjectUpdatableFieldsEquals(returnedExamSubject, getPersistedExamSubject(returnedExamSubject));

        insertedExamSubject = returnedExamSubject;
    }

    @Test
    @Transactional
    void createExamSubjectWithExistingId() throws Exception {
        // Create the ExamSubject with an existing ID
        examSubject.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restExamSubjectMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(examSubject)))
            .andExpect(status().isBadRequest());

        // Validate the ExamSubject in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllExamSubjects() throws Exception {
        // Initialize the database
        insertedExamSubject = examSubjectRepository.saveAndFlush(examSubject);

        // Get all the examSubjectList
        restExamSubjectMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(examSubject.getId().intValue())))
            .andExpect(jsonPath("$.[*].maxMarks").value(hasItem(DEFAULT_MAX_MARKS)));
    }

    @Test
    @Transactional
    void getExamSubject() throws Exception {
        // Initialize the database
        insertedExamSubject = examSubjectRepository.saveAndFlush(examSubject);

        // Get the examSubject
        restExamSubjectMockMvc
            .perform(get(ENTITY_API_URL_ID, examSubject.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(examSubject.getId().intValue()))
            .andExpect(jsonPath("$.maxMarks").value(DEFAULT_MAX_MARKS));
    }

    @Test
    @Transactional
    void getNonExistingExamSubject() throws Exception {
        // Get the examSubject
        restExamSubjectMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingExamSubject() throws Exception {
        // Initialize the database
        insertedExamSubject = examSubjectRepository.saveAndFlush(examSubject);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the examSubject
        ExamSubject updatedExamSubject = examSubjectRepository.findById(examSubject.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedExamSubject are not directly saved in db
        em.detach(updatedExamSubject);
        updatedExamSubject.maxMarks(UPDATED_MAX_MARKS);

        restExamSubjectMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedExamSubject.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedExamSubject))
            )
            .andExpect(status().isOk());

        // Validate the ExamSubject in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedExamSubjectToMatchAllProperties(updatedExamSubject);
    }

    @Test
    @Transactional
    void putNonExistingExamSubject() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        examSubject.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restExamSubjectMockMvc
            .perform(
                put(ENTITY_API_URL_ID, examSubject.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(examSubject))
            )
            .andExpect(status().isBadRequest());

        // Validate the ExamSubject in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchExamSubject() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        examSubject.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExamSubjectMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(examSubject))
            )
            .andExpect(status().isBadRequest());

        // Validate the ExamSubject in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamExamSubject() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        examSubject.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExamSubjectMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(examSubject)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ExamSubject in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateExamSubjectWithPatch() throws Exception {
        // Initialize the database
        insertedExamSubject = examSubjectRepository.saveAndFlush(examSubject);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the examSubject using partial update
        ExamSubject partialUpdatedExamSubject = new ExamSubject();
        partialUpdatedExamSubject.setId(examSubject.getId());

        restExamSubjectMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedExamSubject.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedExamSubject))
            )
            .andExpect(status().isOk());

        // Validate the ExamSubject in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertExamSubjectUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedExamSubject, examSubject),
            getPersistedExamSubject(examSubject)
        );
    }

    @Test
    @Transactional
    void fullUpdateExamSubjectWithPatch() throws Exception {
        // Initialize the database
        insertedExamSubject = examSubjectRepository.saveAndFlush(examSubject);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the examSubject using partial update
        ExamSubject partialUpdatedExamSubject = new ExamSubject();
        partialUpdatedExamSubject.setId(examSubject.getId());

        partialUpdatedExamSubject.maxMarks(UPDATED_MAX_MARKS);

        restExamSubjectMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedExamSubject.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedExamSubject))
            )
            .andExpect(status().isOk());

        // Validate the ExamSubject in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertExamSubjectUpdatableFieldsEquals(partialUpdatedExamSubject, getPersistedExamSubject(partialUpdatedExamSubject));
    }

    @Test
    @Transactional
    void patchNonExistingExamSubject() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        examSubject.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restExamSubjectMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, examSubject.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(examSubject))
            )
            .andExpect(status().isBadRequest());

        // Validate the ExamSubject in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchExamSubject() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        examSubject.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExamSubjectMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(examSubject))
            )
            .andExpect(status().isBadRequest());

        // Validate the ExamSubject in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamExamSubject() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        examSubject.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExamSubjectMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(examSubject)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ExamSubject in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteExamSubject() throws Exception {
        // Initialize the database
        insertedExamSubject = examSubjectRepository.saveAndFlush(examSubject);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the examSubject
        restExamSubjectMockMvc
            .perform(delete(ENTITY_API_URL_ID, examSubject.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return examSubjectRepository.count();
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

    protected ExamSubject getPersistedExamSubject(ExamSubject examSubject) {
        return examSubjectRepository.findById(examSubject.getId()).orElseThrow();
    }

    protected void assertPersistedExamSubjectToMatchAllProperties(ExamSubject expectedExamSubject) {
        assertExamSubjectAllPropertiesEquals(expectedExamSubject, getPersistedExamSubject(expectedExamSubject));
    }

    protected void assertPersistedExamSubjectToMatchUpdatableProperties(ExamSubject expectedExamSubject) {
        assertExamSubjectAllUpdatablePropertiesEquals(expectedExamSubject, getPersistedExamSubject(expectedExamSubject));
    }
}
