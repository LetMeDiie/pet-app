package kz.amihady.app.customer.service.request;


import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public record CustomerUpdateRequest(
        @Size(min = 2, max = 10, message = "Имя должно содержать от 2 до 10 символов.")
        String firstName,

        @Size(min = 2, max = 10, message = "Фамилия должна содержать от 3 до 15 символов.")
        String lastName,

        @Email(message = "Некорректный формат email.")
        String email,

        @Valid
        AddressUpdateRequest address
){
}

