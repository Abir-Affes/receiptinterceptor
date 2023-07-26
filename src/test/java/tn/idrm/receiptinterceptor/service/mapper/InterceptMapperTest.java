package tn.idrm.receiptinterceptor.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class InterceptMapperTest {

    private InterceptMapper interceptMapper;

    @BeforeEach
    public void setUp() {
        interceptMapper = new InterceptMapperImpl();
    }
}
