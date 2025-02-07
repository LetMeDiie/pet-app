package kz.amihady.app.customer.service.response;

import lombok.Builder;

@Builder
public record AddressResponse(
         String street,
         String houseNumber,
         String zipCode
) {
}
