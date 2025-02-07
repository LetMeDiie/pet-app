package kz.amihady.app.customer.controller;

import jakarta.validation.Valid;
import kz.amihady.app.customer.service.CustomerService;
import kz.amihady.app.customer.service.request.CustomerUpdateRequest;
import kz.amihady.app.customer.service.response.CustomerResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/customers/{customer-id:[0-9]+}")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CustomerRestController {
    CustomerService customerService;

    @GetMapping
    public ResponseEntity<CustomerResponse> findById(
            @PathVariable("customer-id") Long customerId){
        return ResponseEntity.ok(customerService.findById(customerId));
    }

    @DeleteMapping
    public ResponseEntity<Void> delete(
            @PathVariable("customer-id") Long customerId){
        customerService.deleteCustomer(customerId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/exists")
    public ResponseEntity<Boolean> existsById(
            @PathVariable("customer-id") Long customerId){
        return ResponseEntity.ok(customerService.existsById(customerId));
    }

    @PutMapping
    public ResponseEntity<Void> updateCustomer(
            @PathVariable("customer-id") Long customerId,
            @RequestBody @Valid CustomerUpdateRequest request){
        customerService.updateCustomer(customerId,request);
        return ResponseEntity.accepted().build();
    }
}
