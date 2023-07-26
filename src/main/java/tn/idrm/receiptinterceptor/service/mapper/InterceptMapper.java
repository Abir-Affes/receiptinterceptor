package tn.idrm.receiptinterceptor.service.mapper;

import org.mapstruct.*;
import tn.idrm.receiptinterceptor.domain.Intercept;
import tn.idrm.receiptinterceptor.service.dto.InterceptDTO;

/**
 * Mapper for the entity {@link Intercept} and its DTO {@link InterceptDTO}.
 */
@Mapper(componentModel = "spring")
public interface InterceptMapper extends EntityMapper<InterceptDTO, Intercept> {}
