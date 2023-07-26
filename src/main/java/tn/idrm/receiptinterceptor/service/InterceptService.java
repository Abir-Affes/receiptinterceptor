package tn.idrm.receiptinterceptor.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.idrm.receiptinterceptor.client.ReceiptConstructorFeignClient;
import tn.idrm.receiptinterceptor.domain.Intercept;
import tn.idrm.receiptinterceptor.repository.InterceptRepository;
import tn.idrm.receiptinterceptor.service.dto.InterceptDTO;
import tn.idrm.receiptinterceptor.service.dto.ReceiptDTO;
import tn.idrm.receiptinterceptor.service.mapper.InterceptMapper;

/**
 * Service Implementation for managing {@link Intercept}.
 */
@Service
@Transactional
public class InterceptService {

    private final Logger log = LoggerFactory.getLogger(InterceptService.class);

    private final InterceptRepository interceptRepository;

    private final InterceptMapper interceptMapper;

    private final ReceiptConstructorFeignClient receiptConstructorFeignClient;

    public InterceptService(
        InterceptRepository interceptRepository,
        InterceptMapper interceptMapper,
        ReceiptConstructorFeignClient receiptConstructorFeignClient
    ) {
        this.interceptRepository = interceptRepository;
        this.interceptMapper = interceptMapper;
        this.receiptConstructorFeignClient = receiptConstructorFeignClient;
    }

    /**
     * Save a intercept.
     *
     * @param interceptDTO the entity to save.
     * @return the persisted entity.
     */
    public InterceptDTO save(InterceptDTO interceptDTO) {
        log.debug("Request to save Intercept : {}", interceptDTO);
        Intercept intercept = interceptMapper.toEntity(interceptDTO);
        intercept = interceptRepository.save(intercept);
        return interceptMapper.toDto(intercept);
    }

    /**
     * Update a intercept.
     *
     * @param interceptDTO the entity to save.
     * @return the persisted entity.
     */
    public InterceptDTO update(InterceptDTO interceptDTO) {
        log.debug("Request to update Intercept : {}", interceptDTO);
        Intercept intercept = interceptMapper.toEntity(interceptDTO);
        intercept = interceptRepository.save(intercept);
        return interceptMapper.toDto(intercept);
    }

    /**
     * Partially update a intercept.
     *
     * @param interceptDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<InterceptDTO> partialUpdate(InterceptDTO interceptDTO) {
        log.debug("Request to partially update Intercept : {}", interceptDTO);

        return interceptRepository
            .findById(interceptDTO.getId())
            .map(existingIntercept -> {
                interceptMapper.partialUpdate(existingIntercept, interceptDTO);

                return existingIntercept;
            })
            .map(interceptRepository::save)
            .map(interceptMapper::toDto);
    }

    /**
     * Get all the intercepts.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<InterceptDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Intercepts");
        return interceptRepository.findAll(pageable).map(interceptMapper::toDto);
    }

    /**
     * Get one intercept by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<InterceptDTO> findOne(Long id) {
        log.debug("Request to get Intercept : {}", id);
        Optional<InterceptDTO> oidto = interceptRepository.findById(id).map(interceptMapper::toDto);
        ReceiptDTO rdto = receiptConstructorFeignClient.getReceipt(oidto.get().getReceipt_code()).getBody();
        oidto.get().setReceiptDTO(rdto);
        return oidto;
    }

    /**
     * Delete the intercept by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Intercept : {}", id);
        interceptRepository.deleteById(id);
    }
}
