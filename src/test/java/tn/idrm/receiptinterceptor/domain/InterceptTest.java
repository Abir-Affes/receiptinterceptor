package tn.idrm.receiptinterceptor.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import tn.idrm.receiptinterceptor.web.rest.TestUtil;

class InterceptTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Intercept.class);
        Intercept intercept1 = new Intercept();
        intercept1.setId(1L);
        Intercept intercept2 = new Intercept();
        intercept2.setId(intercept1.getId());
        assertThat(intercept1).isEqualTo(intercept2);
        intercept2.setId(2L);
        assertThat(intercept1).isNotEqualTo(intercept2);
        intercept1.setId(null);
        assertThat(intercept1).isNotEqualTo(intercept2);
    }
}
