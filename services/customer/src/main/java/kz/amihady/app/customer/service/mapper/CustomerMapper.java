package kz.amihady.app.customer.service.mapper;

import kz.amihady.app.customer.entity.Address;
import kz.amihady.app.customer.entity.Customer;
import kz.amihady.app.customer.service.request.CustomerCreateRequest;
import kz.amihady.app.customer.service.request.CustomerUpdateRequest;
import kz.amihady.app.customer.service.response.AddressResponse;
import kz.amihady.app.customer.service.response.CustomerResponse;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CustomerMapper {

    @Autowired
    private AddressMapper addressMapper;

    public Customer toEntity(CustomerCreateRequest request){
        Address address = addressMapper.toEntity(request.address());

        return Customer.builder()
                .address(address)
                .email(request.email())
                .firstName(request.firstName())
                .lastName(request.lastName())
                .build();
    }

    public CustomerResponse fromCustomer(Customer customer){
        AddressResponse address = addressMapper.fromAddress(customer.getAddress());
        return CustomerResponse.builder()
                .address(address)
                .email(customer.getEmail())
                .firstName(customer.getFirstName())
                .lastName(customer.getLastName())
                .id(customer.getId())
                .build();
    }

    public void updateCustomerFromRequest(Customer customer, CustomerUpdateRequest request){
        if (StringUtils.isNotBlank(request.firstName())) {
            customer.setFirstName(request.firstName());
        }
        if (StringUtils.isNotBlank(request.email())) {
            customer.setEmail(request.email());
        }

        if(StringUtils.isNotBlank(request.lastName())){
            customer.setEmail(request.email());
        }

        if (request.address() != null) {
            addressMapper.updateAddressFromRequest(customer.getAddress(),request.address());
        }

    }
}
