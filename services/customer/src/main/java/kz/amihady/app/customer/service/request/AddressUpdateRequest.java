package kz.amihady.app.customer.service.request;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record AddressUpdateRequest(
        @Size(min = 2, max = 100, message = "Название улицы должно содержать от 2 до 100 символов.")
        String street,

        @Size(min = 1, max = 10, message = "Номер дома должен содержать от 1 до 10 символов.")
        String houseNumber,

        @Pattern(regexp = "\\d{5,6}", message = "Почтовый индекс должен содержать 5 или 6 цифр.")
        String zipCode
) {
}
