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

    @NotBlank(message = "First name is a mandatory field")
    @Size(max = 50, message = "First name must not exceed 50 characters in length")
    private String firstName;

    @NotBlank(message = "Last name is a mandatory field")
    @Size(max = 50, message = "Last name must not exceed 50 characters in length")
    private String lastName;

    @NotBlank(message = "Phone number is a mandatory field")
    @Size(max = 20, message = "Phone number must not exceed 20 characters in length")
    @Pattern(regexp = "[0-9]{10}$", message = "Phone number format is invalid")
    private String phoneNumber;

    @Email(message = "A valid email address is required")
    @NotBlank(message = "Email is a mandatory field")
    @Size(max = 100, message = "Email must not exceed 100 characters in length")
    private String email;

    @NotBlank(message = "Address line 1 is a mandatory field")
    @Size(max = 255, message = "Address line 1 must not exceed 255 characters in length")
    private String addressLine1;

    @Size(max = 255, message = "Address line 2 must not exceed 255 characters in length")
    private String addressLine2;

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
