package com.gevernova.addressbook.mapper;

import com.gevernova.addressbook.dto.AddressRequestDTO;
import com.gevernova.addressbook.dto.AddressResponseDTO;
import com.gevernova.addressbook.dto.UserRequestDTO;
import com.gevernova.addressbook.dto.UserResponseDTO;
import com.gevernova.addressbook.entity.Address;
import com.gevernova.addressbook.entity.User;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class UserMapper {

    // Converts User entity to UserResponseDTO
    public static UserResponseDTO convertToUserDTO(User user) {
        if (user == null) {
            return null;
        }

        UserResponseDTO dto = new UserResponseDTO();
        dto.setId(user.getId());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setPhoneNumber(user.getPhoneNumber());
        dto.setEmail(user.getEmail());

        // Map addresses from entity to DTO list
        dto.setAddresses(Optional.ofNullable(user.getAddresses())
                .orElse(Collections.emptyList())
                .stream()
                .map(UserMapper::convertToAddressDTO) // Use helper for Address mapping
                .collect(Collectors.toList()));

        return dto;
    }

    // Converts UserRequestDTO to User entity
    public static User convertToUserEntity(UserRequestDTO userRequestDTO) {
        if (userRequestDTO == null) {
            return null;
        }

        User user = new User();
        // ID will be set by JPA for new entities, or handled in update logic
        user.setFirstName(userRequestDTO.getFirstName());
        user.setLastName(userRequestDTO.getLastName());
        user.setPhoneNumber(userRequestDTO.getPhoneNumber());
        user.setEmail(userRequestDTO.getEmail());

        // Map addresses from DTO to entity list
        user.setAddresses(Optional.ofNullable(userRequestDTO.getAddresses())
                .orElse(Collections.emptyList())
                .stream()
                .map(UserMapper::convertToAddressEntity) // Use helper for Address mapping
                .collect(Collectors.toList()));

        return user;
    }

    // Helper method: Converts Address entity to AddressResponseDTO
    private static AddressResponseDTO convertToAddressDTO(Address address) {
        if (address == null) {
            return null;
        }
        AddressResponseDTO dto = new AddressResponseDTO();
        dto.setId(address.getId());
        dto.setStreet(address.getStreet()); // Map to new 'street' field
        dto.setAddressLineTwo(address.getAddressLine2());
        dto.setCity(address.getCity());
        dto.setState(address.getState());
        dto.setZipCode(address.getZipCode());
        dto.setCountry(address.getCountry());
        dto.setTags(address.getTags());
        return dto;
    }

    // Helper method: Converts AddressRequestDTO to Address entity
    public static Address convertToAddressEntity(AddressRequestDTO addressRequestDTO) {
        if (addressRequestDTO == null) {
            return null;
        }
        Address address = new Address();
        // ID should not be set here for new addresses; it's managed by JPA.
        // For existing addresses in an update scenario, the ID is typically passed via the main entity/DTO.
        address.setStreet(addressRequestDTO.getStreet()); // Map from new 'street' field
        address.setAddressLine2(addressRequestDTO.getAddressLineTwo());
        address.setCity(addressRequestDTO.getCity());
        address.setState(addressRequestDTO.getState());
        address.setZipCode(addressRequestDTO.getZipCode());
        address.setCountry(addressRequestDTO.getCountry());
        address.setTags(addressRequestDTO.getTags());
        return address;
    }
}