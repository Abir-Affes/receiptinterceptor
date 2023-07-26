package tn.idrm.receiptinterceptor.client;

import feign.Headers;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import tn.idrm.receiptinterceptor.service.dto.ReceiptDTO;

@FeignClient(name = "localhost:8081")
@Headers({ "Accept: application/json", "Content-Type: application/json" })
public interface ReceiptConstructorFeignClient {
    @GetMapping("/api/receipts/{id}")
    public ResponseEntity<ReceiptDTO> getReceipt(@PathVariable("id") Long id);
}
