package kz.amihady.app.customer.service;


import kz.amihady.app.customer.service.request.CustomerCreateRequest;
import kz.amihady.app.customer.service.request.CustomerUpdateRequest;
import kz.amihady.app.customer.service.response.CustomerResponse;

import java.util.List;

public interface CustomerService {
    List<CustomerResponse> findAllCustomers();
    CustomerResponse createCustomer(CustomerCreateRequest request);
    void updateCustomer(Long id,CustomerUpdateRequest request);
    CustomerResponse findById(Long id);
    void deleteCustomer(Long id);
    Boolean existsById(Long id);
}
