package com.gevernova.addressbook.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

// This DTO will hold the actual address properties to be returned in the 'data' field
// of the AddressResponseDTO (your wrapper DTO).
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddressResponseDTO {
    private Long id;
    private String street;
    private String addressLineTwo;
    private String city;
    private String state;
    private String country;
    private String zipCode;
    private String phoneNumber;
    private String email;
    private List<String> tags;
}
