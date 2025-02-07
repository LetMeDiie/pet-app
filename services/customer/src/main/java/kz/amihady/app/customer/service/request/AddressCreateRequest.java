package kz.amihady.app.customer.service.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record AddressCreateRequest(
        @NotNull(message = "Улица не может быть отсутствовать.")
        @NotBlank(message = "Улица не должна быть пустой.")
        @Size(min = 2, max = 100, message = "Название улицы должно содержать от 2 до 100 символов.")
        String street,

        @NotNull(message = "Номер дома не может быть отсутствовать.")
        @NotBlank(message = "Номер дома не должен быть пустым.")
        @Size(min = 1, max = 10, message = "Номер дома должен содержать от 1 до 10 символов.")
        String houseNumber,

        @NotNull(message = "Почтовый индекс не может быть отсутствовать.")
        @NotBlank(message = "Почтовый индекс не должен быть пустым.")
        @Pattern(regexp = "\\d{5,6}", message = "Почтовый индекс должен содержать 5 или 6 цифр.")
        String zipCode
) {
}
