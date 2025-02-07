package kz.amihady.app.customer.service.response;

import lombok.Builder;

@Builder
public record CustomerResponse(
        Long id,
        String firstName,
        String lastName,
        String email,
        AddressResponse address
) {
}
