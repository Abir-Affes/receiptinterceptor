package tn.idrm.receiptinterceptor.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import tn.idrm.receiptinterceptor.IntegrationTest;
import tn.idrm.receiptinterceptor.domain.Intercept;
import tn.idrm.receiptinterceptor.repository.InterceptRepository;
import tn.idrm.receiptinterceptor.service.dto.InterceptDTO;
import tn.idrm.receiptinterceptor.service.mapper.InterceptMapper;

/**
 * Integration tests for the {@link InterceptResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class InterceptResourceIT {

    private static final String DEFAULT_LOCATION = "AAAAAAAAAA";
    private static final String UPDATED_LOCATION = "BBBBBBBBBB";

    private static final Long DEFAULT_RECEIPT_CODE = 1L;
    private static final Long UPDATED_RECEIPT_CODE = 2L;

    private static final String ENTITY_API_URL = "/api/intercepts";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private InterceptRepository interceptRepository;

    @Autowired
    private InterceptMapper interceptMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restInterceptMockMvc;

    private Intercept intercept;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Intercept createEntity(EntityManager em) {
        Intercept intercept = new Intercept().location(DEFAULT_LOCATION).receipt_code(DEFAULT_RECEIPT_CODE);
        return intercept;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Intercept createUpdatedEntity(EntityManager em) {
        Intercept intercept = new Intercept().location(UPDATED_LOCATION).receipt_code(UPDATED_RECEIPT_CODE);
        return intercept;
    }

    @BeforeEach
    public void initTest() {
        intercept = createEntity(em);
    }

    @Test
    @Transactional
    void createIntercept() throws Exception {
        int databaseSizeBeforeCreate = interceptRepository.findAll().size();
        // Create the Intercept
        InterceptDTO interceptDTO = interceptMapper.toDto(intercept);
        restInterceptMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(interceptDTO)))
            .andExpect(status().isCreated());

        // Validate the Intercept in the database
        List<Intercept> interceptList = interceptRepository.findAll();
        assertThat(interceptList).hasSize(databaseSizeBeforeCreate + 1);
        Intercept testIntercept = interceptList.get(interceptList.size() - 1);
        assertThat(testIntercept.getLocation()).isEqualTo(DEFAULT_LOCATION);
        assertThat(testIntercept.getReceipt_code()).isEqualTo(DEFAULT_RECEIPT_CODE);
    }

    @Test
    @Transactional
    void createInterceptWithExistingId() throws Exception {
        // Create the Intercept with an existing ID
        intercept.setId(1L);
        InterceptDTO interceptDTO = interceptMapper.toDto(intercept);

        int databaseSizeBeforeCreate = interceptRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restInterceptMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(interceptDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Intercept in the database
        List<Intercept> interceptList = interceptRepository.findAll();
        assertThat(interceptList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllIntercepts() throws Exception {
        // Initialize the database
        interceptRepository.saveAndFlush(intercept);

        // Get all the interceptList
        restInterceptMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(intercept.getId().intValue())))
            .andExpect(jsonPath("$.[*].location").value(hasItem(DEFAULT_LOCATION)))
            .andExpect(jsonPath("$.[*].receipt_code").value(hasItem(DEFAULT_RECEIPT_CODE.intValue())));
    }

    @Test
    @Transactional
    void getIntercept() throws Exception {
        // Initialize the database
        interceptRepository.saveAndFlush(intercept);

        // Get the intercept
        restInterceptMockMvc
            .perform(get(ENTITY_API_URL_ID, intercept.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(intercept.getId().intValue()))
            .andExpect(jsonPath("$.location").value(DEFAULT_LOCATION))
            .andExpect(jsonPath("$.receipt_code").value(DEFAULT_RECEIPT_CODE.intValue()));
    }

    @Test
    @Transactional
    void getNonExistingIntercept() throws Exception {
        // Get the intercept
        restInterceptMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingIntercept() throws Exception {
        // Initialize the database
        interceptRepository.saveAndFlush(intercept);

        int databaseSizeBeforeUpdate = interceptRepository.findAll().size();

        // Update the intercept
        Intercept updatedIntercept = interceptRepository.findById(intercept.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedIntercept are not directly saved in db
        em.detach(updatedIntercept);
        updatedIntercept.location(UPDATED_LOCATION).receipt_code(UPDATED_RECEIPT_CODE);
        InterceptDTO interceptDTO = interceptMapper.toDto(updatedIntercept);

        restInterceptMockMvc
            .perform(
                put(ENTITY_API_URL_ID, interceptDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(interceptDTO))
            )
            .andExpect(status().isOk());

        // Validate the Intercept in the database
        List<Intercept> interceptList = interceptRepository.findAll();
        assertThat(interceptList).hasSize(databaseSizeBeforeUpdate);
        Intercept testIntercept = interceptList.get(interceptList.size() - 1);
        assertThat(testIntercept.getLocation()).isEqualTo(UPDATED_LOCATION);
        assertThat(testIntercept.getReceipt_code()).isEqualTo(UPDATED_RECEIPT_CODE);
    }

    @Test
    @Transactional
    void putNonExistingIntercept() throws Exception {
        int databaseSizeBeforeUpdate = interceptRepository.findAll().size();
        intercept.setId(count.incrementAndGet());

        // Create the Intercept
        InterceptDTO interceptDTO = interceptMapper.toDto(intercept);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInterceptMockMvc
            .perform(
                put(ENTITY_API_URL_ID, interceptDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(interceptDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Intercept in the database
        List<Intercept> interceptList = interceptRepository.findAll();
        assertThat(interceptList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchIntercept() throws Exception {
        int databaseSizeBeforeUpdate = interceptRepository.findAll().size();
        intercept.setId(count.incrementAndGet());

        // Create the Intercept
        InterceptDTO interceptDTO = interceptMapper.toDto(intercept);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInterceptMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(interceptDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Intercept in the database
        List<Intercept> interceptList = interceptRepository.findAll();
        assertThat(interceptList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamIntercept() throws Exception {
        int databaseSizeBeforeUpdate = interceptRepository.findAll().size();
        intercept.setId(count.incrementAndGet());

        // Create the Intercept
        InterceptDTO interceptDTO = interceptMapper.toDto(intercept);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInterceptMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(interceptDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Intercept in the database
        List<Intercept> interceptList = interceptRepository.findAll();
        assertThat(interceptList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateInterceptWithPatch() throws Exception {
        // Initialize the database
        interceptRepository.saveAndFlush(intercept);

        int databaseSizeBeforeUpdate = interceptRepository.findAll().size();

        // Update the intercept using partial update
        Intercept partialUpdatedIntercept = new Intercept();
        partialUpdatedIntercept.setId(intercept.getId());

        restInterceptMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedIntercept.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedIntercept))
            )
            .andExpect(status().isOk());

        // Validate the Intercept in the database
        List<Intercept> interceptList = interceptRepository.findAll();
        assertThat(interceptList).hasSize(databaseSizeBeforeUpdate);
        Intercept testIntercept = interceptList.get(interceptList.size() - 1);
        assertThat(testIntercept.getLocation()).isEqualTo(DEFAULT_LOCATION);
        assertThat(testIntercept.getReceipt_code()).isEqualTo(DEFAULT_RECEIPT_CODE);
    }

    @Test
    @Transactional
    void fullUpdateInterceptWithPatch() throws Exception {
        // Initialize the database
        interceptRepository.saveAndFlush(intercept);

        int databaseSizeBeforeUpdate = interceptRepository.findAll().size();

        // Update the intercept using partial update
        Intercept partialUpdatedIntercept = new Intercept();
        partialUpdatedIntercept.setId(intercept.getId());

        partialUpdatedIntercept.location(UPDATED_LOCATION).receipt_code(UPDATED_RECEIPT_CODE);

        restInterceptMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedIntercept.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedIntercept))
            )
            .andExpect(status().isOk());

        // Validate the Intercept in the database
        List<Intercept> interceptList = interceptRepository.findAll();
        assertThat(interceptList).hasSize(databaseSizeBeforeUpdate);
        Intercept testIntercept = interceptList.get(interceptList.size() - 1);
        assertThat(testIntercept.getLocation()).isEqualTo(UPDATED_LOCATION);
        assertThat(testIntercept.getReceipt_code()).isEqualTo(UPDATED_RECEIPT_CODE);
    }

    @Test
    @Transactional
    void patchNonExistingIntercept() throws Exception {
        int databaseSizeBeforeUpdate = interceptRepository.findAll().size();
        intercept.setId(count.incrementAndGet());

        // Create the Intercept
        InterceptDTO interceptDTO = interceptMapper.toDto(intercept);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInterceptMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, interceptDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(interceptDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Intercept in the database
        List<Intercept> interceptList = interceptRepository.findAll();
        assertThat(interceptList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchIntercept() throws Exception {
        int databaseSizeBeforeUpdate = interceptRepository.findAll().size();
        intercept.setId(count.incrementAndGet());

        // Create the Intercept
        InterceptDTO interceptDTO = interceptMapper.toDto(intercept);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInterceptMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(interceptDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Intercept in the database
        List<Intercept> interceptList = interceptRepository.findAll();
        assertThat(interceptList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamIntercept() throws Exception {
        int databaseSizeBeforeUpdate = interceptRepository.findAll().size();
        intercept.setId(count.incrementAndGet());

        // Create the Intercept
        InterceptDTO interceptDTO = interceptMapper.toDto(intercept);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInterceptMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(interceptDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Intercept in the database
        List<Intercept> interceptList = interceptRepository.findAll();
        assertThat(interceptList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteIntercept() throws Exception {
        // Initialize the database
        interceptRepository.saveAndFlush(intercept);

        int databaseSizeBeforeDelete = interceptRepository.findAll().size();

        // Delete the intercept
        restInterceptMockMvc
            .perform(delete(ENTITY_API_URL_ID, intercept.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Intercept> interceptList = interceptRepository.findAll();
        assertThat(interceptList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
