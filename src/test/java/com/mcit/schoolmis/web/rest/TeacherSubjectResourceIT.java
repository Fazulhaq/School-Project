package com.mcit.schoolmis.web.rest;

import static com.mcit.schoolmis.domain.TeacherSubjectAsserts.*;
import static com.mcit.schoolmis.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mcit.schoolmis.IntegrationTest;
import com.mcit.schoolmis.domain.TeacherSubject;
import com.mcit.schoolmis.repository.TeacherSubjectRepository;
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
 * Integration tests for the {@link TeacherSubjectResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TeacherSubjectResourceIT {

    private static final String ENTITY_API_URL = "/api/teacher-subjects";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private TeacherSubjectRepository teacherSubjectRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTeacherSubjectMockMvc;

    private TeacherSubject teacherSubject;

    private TeacherSubject insertedTeacherSubject;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TeacherSubject createEntity(EntityManager em) {
        TeacherSubject teacherSubject = new TeacherSubject();
        return teacherSubject;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TeacherSubject createUpdatedEntity(EntityManager em) {
        TeacherSubject teacherSubject = new TeacherSubject();
        return teacherSubject;
    }

    @BeforeEach
    public void initTest() {
        teacherSubject = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedTeacherSubject != null) {
            teacherSubjectRepository.delete(insertedTeacherSubject);
            insertedTeacherSubject = null;
        }
    }

    @Test
    @Transactional
    void createTeacherSubject() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the TeacherSubject
        var returnedTeacherSubject = om.readValue(
            restTeacherSubjectMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(teacherSubject)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            TeacherSubject.class
        );

        // Validate the TeacherSubject in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertTeacherSubjectUpdatableFieldsEquals(returnedTeacherSubject, getPersistedTeacherSubject(returnedTeacherSubject));

        insertedTeacherSubject = returnedTeacherSubject;
    }

    @Test
    @Transactional
    void createTeacherSubjectWithExistingId() throws Exception {
        // Create the TeacherSubject with an existing ID
        teacherSubject.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTeacherSubjectMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(teacherSubject)))
            .andExpect(status().isBadRequest());

        // Validate the TeacherSubject in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllTeacherSubjects() throws Exception {
        // Initialize the database
        insertedTeacherSubject = teacherSubjectRepository.saveAndFlush(teacherSubject);

        // Get all the teacherSubjectList
        restTeacherSubjectMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(teacherSubject.getId().intValue())));
    }

    @Test
    @Transactional
    void getTeacherSubject() throws Exception {
        // Initialize the database
        insertedTeacherSubject = teacherSubjectRepository.saveAndFlush(teacherSubject);

        // Get the teacherSubject
        restTeacherSubjectMockMvc
            .perform(get(ENTITY_API_URL_ID, teacherSubject.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(teacherSubject.getId().intValue()));
    }

    @Test
    @Transactional
    void getNonExistingTeacherSubject() throws Exception {
        // Get the teacherSubject
        restTeacherSubjectMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingTeacherSubject() throws Exception {
        // Initialize the database
        insertedTeacherSubject = teacherSubjectRepository.saveAndFlush(teacherSubject);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the teacherSubject
        TeacherSubject updatedTeacherSubject = teacherSubjectRepository.findById(teacherSubject.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedTeacherSubject are not directly saved in db
        em.detach(updatedTeacherSubject);

        restTeacherSubjectMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedTeacherSubject.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedTeacherSubject))
            )
            .andExpect(status().isOk());

        // Validate the TeacherSubject in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedTeacherSubjectToMatchAllProperties(updatedTeacherSubject);
    }

    @Test
    @Transactional
    void putNonExistingTeacherSubject() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        teacherSubject.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTeacherSubjectMockMvc
            .perform(
                put(ENTITY_API_URL_ID, teacherSubject.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(teacherSubject))
            )
            .andExpect(status().isBadRequest());

        // Validate the TeacherSubject in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTeacherSubject() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        teacherSubject.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTeacherSubjectMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(teacherSubject))
            )
            .andExpect(status().isBadRequest());

        // Validate the TeacherSubject in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTeacherSubject() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        teacherSubject.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTeacherSubjectMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(teacherSubject)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TeacherSubject in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTeacherSubjectWithPatch() throws Exception {
        // Initialize the database
        insertedTeacherSubject = teacherSubjectRepository.saveAndFlush(teacherSubject);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the teacherSubject using partial update
        TeacherSubject partialUpdatedTeacherSubject = new TeacherSubject();
        partialUpdatedTeacherSubject.setId(teacherSubject.getId());

        restTeacherSubjectMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTeacherSubject.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTeacherSubject))
            )
            .andExpect(status().isOk());

        // Validate the TeacherSubject in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTeacherSubjectUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedTeacherSubject, teacherSubject),
            getPersistedTeacherSubject(teacherSubject)
        );
    }

    @Test
    @Transactional
    void fullUpdateTeacherSubjectWithPatch() throws Exception {
        // Initialize the database
        insertedTeacherSubject = teacherSubjectRepository.saveAndFlush(teacherSubject);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the teacherSubject using partial update
        TeacherSubject partialUpdatedTeacherSubject = new TeacherSubject();
        partialUpdatedTeacherSubject.setId(teacherSubject.getId());

        restTeacherSubjectMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTeacherSubject.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTeacherSubject))
            )
            .andExpect(status().isOk());

        // Validate the TeacherSubject in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTeacherSubjectUpdatableFieldsEquals(partialUpdatedTeacherSubject, getPersistedTeacherSubject(partialUpdatedTeacherSubject));
    }

    @Test
    @Transactional
    void patchNonExistingTeacherSubject() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        teacherSubject.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTeacherSubjectMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, teacherSubject.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(teacherSubject))
            )
            .andExpect(status().isBadRequest());

        // Validate the TeacherSubject in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTeacherSubject() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        teacherSubject.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTeacherSubjectMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(teacherSubject))
            )
            .andExpect(status().isBadRequest());

        // Validate the TeacherSubject in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTeacherSubject() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        teacherSubject.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTeacherSubjectMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(teacherSubject)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TeacherSubject in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTeacherSubject() throws Exception {
        // Initialize the database
        insertedTeacherSubject = teacherSubjectRepository.saveAndFlush(teacherSubject);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the teacherSubject
        restTeacherSubjectMockMvc
            .perform(delete(ENTITY_API_URL_ID, teacherSubject.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return teacherSubjectRepository.count();
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

    protected TeacherSubject getPersistedTeacherSubject(TeacherSubject teacherSubject) {
        return teacherSubjectRepository.findById(teacherSubject.getId()).orElseThrow();
    }

    protected void assertPersistedTeacherSubjectToMatchAllProperties(TeacherSubject expectedTeacherSubject) {
        assertTeacherSubjectAllPropertiesEquals(expectedTeacherSubject, getPersistedTeacherSubject(expectedTeacherSubject));
    }

    protected void assertPersistedTeacherSubjectToMatchUpdatableProperties(TeacherSubject expectedTeacherSubject) {
        assertTeacherSubjectAllUpdatablePropertiesEquals(expectedTeacherSubject, getPersistedTeacherSubject(expectedTeacherSubject));
    }
}
