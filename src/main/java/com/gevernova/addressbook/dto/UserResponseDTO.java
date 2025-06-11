package com.gevernova.addressbook.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UserResponseDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String email;
    private List<AddressResponseDTO> addresses; // List of addresses for the user
    //No password is included in the response

    public String getFullName() {
        return this.firstName + " " + this.lastName;
    }
}
