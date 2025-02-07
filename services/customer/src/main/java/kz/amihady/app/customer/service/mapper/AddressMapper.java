package kz.amihady.app.customer.service.mapper;

import kz.amihady.app.customer.entity.Address;
import kz.amihady.app.customer.service.request.AddressCreateRequest;
import kz.amihady.app.customer.service.request.AddressUpdateRequest;
import kz.amihady.app.customer.service.response.AddressResponse;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

@Component
public class AddressMapper {

    public Address toEntity (AddressCreateRequest addressCreateRequest){
        Address address =
                Address.builder()
                        .houseNumber(addressCreateRequest.houseNumber())
                        .street(addressCreateRequest.street())
                        .zipCode(addressCreateRequest.zipCode())
                        .build();
        return address;
    }

    public AddressResponse fromAddress(Address address){
        return AddressResponse.builder()
                .houseNumber(address.getHouseNumber())
                .street(address.getStreet())
                .zipCode(address.getZipCode())
                .build();
    }

    public void updateAddressFromRequest(Address address, AddressUpdateRequest request){
        if (StringUtils.isNotBlank(request.houseNumber())) {
            address.setHouseNumber(request.houseNumber());
        }
        if (StringUtils.isNotBlank(request.zipCode())) {
            address.setZipCode(request.zipCode());
        }

        if(StringUtils.isNotBlank(request.street())){
            address.setStreet(request.street());
        }
    }
}
