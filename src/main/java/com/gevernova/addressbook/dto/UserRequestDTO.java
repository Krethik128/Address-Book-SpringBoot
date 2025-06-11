package com.gevernova.addressbook.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRequestDTO {

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

    @NotBlank(message = "Password is a mandatory field")
    @Size(max = 100, message = "Password must not exceed 100 characters in length")
    private String password;

    @Valid // Ensures that validation rules inside AddressRequestDTO are applied
    private java.util.List<AddressRequestDTO> addresses;

}
