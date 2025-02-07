package kz.amihady.app.customer.entity;

import jakarta.persistence.Entity;
import lombok.*;
import org.springframework.validation.annotation.Validated;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Validated
public class Address {

    private String street;
    private String houseNumber;
    private String zipCode;

}
