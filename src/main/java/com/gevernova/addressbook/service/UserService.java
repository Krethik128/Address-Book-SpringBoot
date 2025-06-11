package com.gevernova.addressbook.service;


import com.gevernova.addressbook.dto.UserRequestDTO;
import com.gevernova.addressbook.dto.UserResponseDTO;

import java.util.List;
import java.util.Optional;

public interface UserService {
    List<UserResponseDTO> getAllUsers();
    Optional<UserResponseDTO> getUserById(Long id);
    UserResponseDTO createUser(UserRequestDTO userRequestDTO); // Changed to accept DTO
    UserResponseDTO updateUser(Long id, UserRequestDTO userRequestDTO); // Changed to accept DTO
    void deleteUser(Long id);

    // New methods for searching and sorting addresses by city/state,
    // returning users who have such addresses.
    List<UserResponseDTO> findUsersByAddressCityAndState(String city, String state);
    List<UserResponseDTO> findUsersByAddressCity(String city);
    List<UserResponseDTO> findUsersByAddressState(String state);
    List<UserResponseDTO> findUsersByAddressCityOrState(String city, String state);
}
