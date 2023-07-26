package tn.idrm.receiptinterceptor.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
import tn.idrm.receiptinterceptor.domain.enumeration.result;
import tn.idrm.receiptinterceptor.domain.enumeration.trans_type;

/**
 * A DTO for the {@link tn.idrm.receiptconstructor.domain.Receipt} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ReceiptDTO implements Serializable {

    private Long id;

    @Max(value = 9999)
    private Integer receipt_no;

    @Max(value = 999999)
    private Integer trace_no;

    @DecimalMax(value = "99999.99")
    private Double amount;

    private trans_type transaction_type;

    private Integer vu_no;

    private String receipt_type;

    private Integer ref_parameters;

    private Integer licensing_no;

    private Integer pos_info;

    private result result;

    private LocalDate date;

    private TerminalDTO terminal_id;

    private CardDTO card_id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getReceipt_no() {
        return receipt_no;
    }

    public void setReceipt_no(Integer receipt_no) {
        this.receipt_no = receipt_no;
    }

    public Integer getTrace_no() {
        return trace_no;
    }

    public void setTrace_no(Integer trace_no) {
        this.trace_no = trace_no;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public trans_type getTransaction_type() {
        return transaction_type;
    }

    public void setTransaction_type(trans_type transaction_type) {
        this.transaction_type = transaction_type;
    }

    public Integer getVu_no() {
        return vu_no;
    }

    public void setVu_no(Integer vu_no) {
        this.vu_no = vu_no;
    }

    public String getReceipt_type() {
        return receipt_type;
    }

    public void setReceipt_type(String receipt_type) {
        this.receipt_type = receipt_type;
    }

    public Integer getRef_parameters() {
        return ref_parameters;
    }

    public void setRef_parameters(Integer ref_parameters) {
        this.ref_parameters = ref_parameters;
    }

    public Integer getLicensing_no() {
        return licensing_no;
    }

    public void setLicensing_no(Integer licensing_no) {
        this.licensing_no = licensing_no;
    }

    public Integer getPos_info() {
        return pos_info;
    }

    public void setPos_info(Integer pos_info) {
        this.pos_info = pos_info;
    }

    public result getResult() {
        return result;
    }

    public void setResult(result result) {
        this.result = result;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public TerminalDTO getTerminal_id() {
        return terminal_id;
    }

    public void setTerminal_id(TerminalDTO terminal_id) {
        this.terminal_id = terminal_id;
    }

    public CardDTO getCard_id() {
        return card_id;
    }

    public void setCard_id(CardDTO card_id) {
        this.card_id = card_id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ReceiptDTO)) {
            return false;
        }

        ReceiptDTO receiptDTO = (ReceiptDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, receiptDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    @Override
    public String toString() {
        return (
            "ReceiptDTO [id=" +
            id +
            ", receipt_no=" +
            receipt_no +
            ", trace_no=" +
            trace_no +
            ", amount=" +
            amount +
            ", transaction_type=" +
            transaction_type +
            ", vu_no=" +
            vu_no +
            ", receipt_type=" +
            receipt_type +
            ", ref_parameters=" +
            ref_parameters +
            ", licensing_no=" +
            licensing_no +
            ", pos_info=" +
            pos_info +
            ", result=" +
            result +
            ", date=" +
            date +
            ", terminal_id=" +
            terminal_id +
            ", card_id=" +
            card_id +
            "]"
        );
    }
}
