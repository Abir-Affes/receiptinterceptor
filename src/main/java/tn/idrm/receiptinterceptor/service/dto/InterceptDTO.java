package tn.idrm.receiptinterceptor.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link tn.idrm.receiptinterceptor.domain.Intercept} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class InterceptDTO implements Serializable {

    private Long id;

    private String location;

    private Long receipt_code;

    private ReceiptDTO receiptDTO;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Long getReceipt_code() {
        return receipt_code;
    }

    public void setReceipt_code(Long receipt_code) {
        this.receipt_code = receipt_code;
    }

    public ReceiptDTO getReceiptDTO() {
        return receiptDTO;
    }

    public void setReceiptDTO(ReceiptDTO receiptDTO2) {
        this.receiptDTO = receiptDTO2;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof InterceptDTO)) {
            return false;
        }

        InterceptDTO interceptDTO = (InterceptDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, interceptDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    @Override
    public String toString() {
        return "InterceptDTO [id=" + id + ", location=" + location + ", receipt_code=" + receipt_code + ", receiptDTO=" + receiptDTO + "]";
    }
    // prettier-ignore

}
