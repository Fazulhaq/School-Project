package com.mcit.schoolmis.web.rest;

import static com.mcit.schoolmis.domain.TimetableAsserts.*;
import static com.mcit.schoolmis.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mcit.schoolmis.IntegrationTest;
import com.mcit.schoolmis.domain.Timetable;
import com.mcit.schoolmis.repository.TimetableRepository;
import jakarta.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
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
 * Integration tests for the {@link TimetableResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TimetableResourceIT {

    private static final String DEFAULT_DAY_OF_WEEK = "AAAAAAAAAA";
    private static final String UPDATED_DAY_OF_WEEK = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_START_TIME = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_START_TIME = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_END_TIME = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_END_TIME = LocalDate.now(ZoneId.systemDefault());

    private static final String ENTITY_API_URL = "/api/timetables";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private TimetableRepository timetableRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTimetableMockMvc;

    private Timetable timetable;

    private Timetable insertedTimetable;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Timetable createEntity(EntityManager em) {
        Timetable timetable = new Timetable().dayOfWeek(DEFAULT_DAY_OF_WEEK).startTime(DEFAULT_START_TIME).endTime(DEFAULT_END_TIME);
        return timetable;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Timetable createUpdatedEntity(EntityManager em) {
        Timetable timetable = new Timetable().dayOfWeek(UPDATED_DAY_OF_WEEK).startTime(UPDATED_START_TIME).endTime(UPDATED_END_TIME);
        return timetable;
    }

    @BeforeEach
    public void initTest() {
        timetable = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedTimetable != null) {
            timetableRepository.delete(insertedTimetable);
            insertedTimetable = null;
        }
    }

    @Test
    @Transactional
    void createTimetable() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Timetable
        var returnedTimetable = om.readValue(
            restTimetableMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(timetable)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Timetable.class
        );

        // Validate the Timetable in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertTimetableUpdatableFieldsEquals(returnedTimetable, getPersistedTimetable(returnedTimetable));

        insertedTimetable = returnedTimetable;
    }

    @Test
    @Transactional
    void createTimetableWithExistingId() throws Exception {
        // Create the Timetable with an existing ID
        timetable.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTimetableMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(timetable)))
            .andExpect(status().isBadRequest());

        // Validate the Timetable in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllTimetables() throws Exception {
        // Initialize the database
        insertedTimetable = timetableRepository.saveAndFlush(timetable);

        // Get all the timetableList
        restTimetableMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(timetable.getId().intValue())))
            .andExpect(jsonPath("$.[*].dayOfWeek").value(hasItem(DEFAULT_DAY_OF_WEEK)))
            .andExpect(jsonPath("$.[*].startTime").value(hasItem(DEFAULT_START_TIME.toString())))
            .andExpect(jsonPath("$.[*].endTime").value(hasItem(DEFAULT_END_TIME.toString())));
    }

    @Test
    @Transactional
    void getTimetable() throws Exception {
        // Initialize the database
        insertedTimetable = timetableRepository.saveAndFlush(timetable);

        // Get the timetable
        restTimetableMockMvc
            .perform(get(ENTITY_API_URL_ID, timetable.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(timetable.getId().intValue()))
            .andExpect(jsonPath("$.dayOfWeek").value(DEFAULT_DAY_OF_WEEK))
            .andExpect(jsonPath("$.startTime").value(DEFAULT_START_TIME.toString()))
            .andExpect(jsonPath("$.endTime").value(DEFAULT_END_TIME.toString()));
    }

    @Test
    @Transactional
    void getNonExistingTimetable() throws Exception {
        // Get the timetable
        restTimetableMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingTimetable() throws Exception {
        // Initialize the database
        insertedTimetable = timetableRepository.saveAndFlush(timetable);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the timetable
        Timetable updatedTimetable = timetableRepository.findById(timetable.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedTimetable are not directly saved in db
        em.detach(updatedTimetable);
        updatedTimetable.dayOfWeek(UPDATED_DAY_OF_WEEK).startTime(UPDATED_START_TIME).endTime(UPDATED_END_TIME);

        restTimetableMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedTimetable.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedTimetable))
            )
            .andExpect(status().isOk());

        // Validate the Timetable in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedTimetableToMatchAllProperties(updatedTimetable);
    }

    @Test
    @Transactional
    void putNonExistingTimetable() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        timetable.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTimetableMockMvc
            .perform(
                put(ENTITY_API_URL_ID, timetable.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(timetable))
            )
            .andExpect(status().isBadRequest());

        // Validate the Timetable in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTimetable() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        timetable.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTimetableMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(timetable))
            )
            .andExpect(status().isBadRequest());

        // Validate the Timetable in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTimetable() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        timetable.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTimetableMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(timetable)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Timetable in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTimetableWithPatch() throws Exception {
        // Initialize the database
        insertedTimetable = timetableRepository.saveAndFlush(timetable);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the timetable using partial update
        Timetable partialUpdatedTimetable = new Timetable();
        partialUpdatedTimetable.setId(timetable.getId());

        partialUpdatedTimetable.startTime(UPDATED_START_TIME);

        restTimetableMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTimetable.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTimetable))
            )
            .andExpect(status().isOk());

        // Validate the Timetable in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTimetableUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedTimetable, timetable),
            getPersistedTimetable(timetable)
        );
    }

    @Test
    @Transactional
    void fullUpdateTimetableWithPatch() throws Exception {
        // Initialize the database
        insertedTimetable = timetableRepository.saveAndFlush(timetable);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the timetable using partial update
        Timetable partialUpdatedTimetable = new Timetable();
        partialUpdatedTimetable.setId(timetable.getId());

        partialUpdatedTimetable.dayOfWeek(UPDATED_DAY_OF_WEEK).startTime(UPDATED_START_TIME).endTime(UPDATED_END_TIME);

        restTimetableMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTimetable.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTimetable))
            )
            .andExpect(status().isOk());

        // Validate the Timetable in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTimetableUpdatableFieldsEquals(partialUpdatedTimetable, getPersistedTimetable(partialUpdatedTimetable));
    }

    @Test
    @Transactional
    void patchNonExistingTimetable() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        timetable.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTimetableMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, timetable.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(timetable))
            )
            .andExpect(status().isBadRequest());

        // Validate the Timetable in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTimetable() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        timetable.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTimetableMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(timetable))
            )
            .andExpect(status().isBadRequest());

        // Validate the Timetable in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTimetable() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        timetable.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTimetableMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(timetable)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Timetable in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTimetable() throws Exception {
        // Initialize the database
        insertedTimetable = timetableRepository.saveAndFlush(timetable);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the timetable
        restTimetableMockMvc
            .perform(delete(ENTITY_API_URL_ID, timetable.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return timetableRepository.count();
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

    protected Timetable getPersistedTimetable(Timetable timetable) {
        return timetableRepository.findById(timetable.getId()).orElseThrow();
    }

    protected void assertPersistedTimetableToMatchAllProperties(Timetable expectedTimetable) {
        assertTimetableAllPropertiesEquals(expectedTimetable, getPersistedTimetable(expectedTimetable));
    }

    protected void assertPersistedTimetableToMatchUpdatableProperties(Timetable expectedTimetable) {
        assertTimetableAllUpdatablePropertiesEquals(expectedTimetable, getPersistedTimetable(expectedTimetable));
    }
}
