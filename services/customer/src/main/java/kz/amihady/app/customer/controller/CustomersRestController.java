package kz.amihady.app.customer.controller;

import jakarta.validation.Valid;
import kz.amihady.app.customer.service.CustomerService;
import kz.amihady.app.customer.service.request.CustomerCreateRequest;
import kz.amihady.app.customer.service.response.CustomerResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/customers")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CustomersRestController {
    CustomerService customerService;

    @PostMapping
    public ResponseEntity<CustomerResponse> createCustomer(
            @RequestBody @Valid CustomerCreateRequest request) {

        CustomerResponse response = customerService.createCustomer(request);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.id())
                .toUri();

        return ResponseEntity.created(location).body(response);
    }

    @GetMapping
    public ResponseEntity<List<CustomerResponse>> findAll(){
        return ResponseEntity.ok(customerService.findAllCustomers());
    }

}
