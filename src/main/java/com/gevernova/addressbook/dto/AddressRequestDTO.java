package com.gevernova.addressbook.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Generated
public class AddressRequestDTO {

    @NotBlank(message = "Address line 1 is a mandatory field")
    @Size(max = 255, message = "Address line 1 must not exceed 255 characters in length")
    private String street;

    @Size(max = 255, message = "Address line 2 must not exceed 255 characters in length")
    private String addressLineTwo;

    @NotBlank(message = "City is a mandatory field")
    @Size(max = 100, message = "City must not exceed 100 characters in length")
    private String city;

    @NotBlank(message = "State is a mandatory field")
    @Size(max = 100, message = "State must not exceed 100 characters in length")
    private String state;

    @NotBlank(message = "Zip code is a mandatory field")
    @Size(max = 10, message = "Zip code must not exceed 10 characters in length")
    private String zipCode;

    @NotBlank(message = "Country is a mandatory field")
    @Size(max = 100, message = "Country must not exceed 100 characters in length")
    private String country;

    private java.util.List<String> tags;
}
