package tn.idrm.receiptinterceptor.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import tn.idrm.receiptinterceptor.web.rest.TestUtil;

class InterceptDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(InterceptDTO.class);
        InterceptDTO interceptDTO1 = new InterceptDTO();
        interceptDTO1.setId(1L);
        InterceptDTO interceptDTO2 = new InterceptDTO();
        assertThat(interceptDTO1).isNotEqualTo(interceptDTO2);
        interceptDTO2.setId(interceptDTO1.getId());
        assertThat(interceptDTO1).isEqualTo(interceptDTO2);
        interceptDTO2.setId(2L);
        assertThat(interceptDTO1).isNotEqualTo(interceptDTO2);
        interceptDTO1.setId(null);
        assertThat(interceptDTO1).isNotEqualTo(interceptDTO2);
    }
}
