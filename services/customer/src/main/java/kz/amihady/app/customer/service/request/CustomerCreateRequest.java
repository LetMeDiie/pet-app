package kz.amihady.app.customer.service.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CustomerCreateRequest(

        @NotNull(message = "Имя не может быть отсутствовать.")
        @NotBlank(message = "Имя не должно быть пустым.")
        @Size(min = 2, max = 10, message = "Имя должно содержать от 2 до 10 символов.")
        String firstName,

        @NotNull(message = "Фамилия не может быть отсутствовать.")
        @NotBlank(message = "Фамилия не должна быть пустой.")
        @Size(min = 2, max = 10, message = "Фамилия должна содержать от 3 до 15 символов.")
        String lastName,

        @NotNull(message = "Email не может быть отсутствовать.")
        @NotBlank(message = "Email не должен быть пустым.")
        @Email(message = "Некорректный формат email.")
        String email,
        @Valid
        @NotNull(message = "Адрес не может отсутствовать.")
        AddressCreateRequest address
) {
}
