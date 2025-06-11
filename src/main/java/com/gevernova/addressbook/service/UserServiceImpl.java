package com.gevernova.addressbook.service;

import com.gevernova.addressbook.dto.UserRequestDTO;
import com.gevernova.addressbook.dto.UserResponseDTO;
import com.gevernova.addressbook.entity.Address;
import com.gevernova.addressbook.entity.User;
import com.gevernova.addressbook.exceptionhandler.UserNotFoundException; // New UserNotFoundException
import com.gevernova.addressbook.mapper.UserMapper;
import com.gevernova.addressbook.repository.AddressRepository;
import com.gevernova.addressbook.repository.UserRepository; // New UserRepository
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserRepository userRepository;
    private final AddressRepository addressRepository;

    // AddressRepository can be omitted here if cascade is sufficient,
    // but useful if you need to directly manage addresses outside of user lifecycle or for complex updates.
    // For simplicity, we'll rely on cascading in this refactoring for basic CRUD.

    private List<UserResponseDTO> convertAddressesToUniqueUserDTOs(List<Address> addresses) {
        Set<User> uniqueUsers = new HashSet<>();
        for (Address address : addresses) {
            if (address.getUser() != null) {
                // Fetch the full user entity to ensure all details are available
                userRepository.findById(address.getUser().getId()).ifPresent(uniqueUsers::add);
            }
        }
        logger.debug("Found {} unique users from matching addresses.", uniqueUsers.size());
        return uniqueUsers.stream()
                .map(UserMapper::convertToUserDTO)
                .collect(Collectors.toList());
    }

    @Autowired // Added back for clarity
    public UserServiceImpl(UserRepository userRepository, AddressRepository addressRepository) {
        this.userRepository = userRepository;
        this.addressRepository = addressRepository;
    }

    @Override
    public List<UserResponseDTO> getAllUsers() {
        logger.debug("Fetching all users from the repository.");
        List<User> users = userRepository.findAll();
        logger.debug("Found {} users.", users.size());
        return users.stream()
                .map(UserMapper::convertToUserDTO) // Use UserMapper
                .collect(Collectors.toList());
    }

    @Override
    public Optional<UserResponseDTO> getUserById(Long id) {
        logger.debug("Attempting to find user with ID: {}.", id);
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            logger.debug("User with ID: {} found.", id);
            return Optional.of(UserMapper.convertToUserDTO(user.get())); // Use UserMapper
        } else {
            logger.debug("User with ID: {} not found in repository.", id);
            return Optional.empty();
        }
    }

    @Override
    public UserResponseDTO createUser(UserRequestDTO userRequestDTO) { // Changed to accept DTO
        logger.info("Saving new user: {} {}.", userRequestDTO.getFirstName(), userRequestDTO.getLastName());
        User userToCreate = UserMapper.convertToUserEntity(userRequestDTO); // Convert DTO to entity
        if (userToCreate.getAddresses() != null) {
            userToCreate.getAddresses().forEach(address -> address.setUser(userToCreate));
        }

        User savedUser = userRepository.save(userToCreate);
        logger.info("New user saved with ID: {}.", savedUser.getId());
        return UserMapper.convertToUserDTO(savedUser); // Convert saved entity to DTO
    }

    @Override
    public UserResponseDTO updateUser(Long id, UserRequestDTO userRequestDTO) { // Changed to accept DTO
        logger.info("Updating user with ID: {}.", id);
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Attempted to update non-existent user with ID: {}.", id);
                    return new UserNotFoundException("User with ID: " + id + " was not found.");
                });

        logger.debug("Updating fields for user ID {}. Old first name: {}, New first name: {}",
                id, existingUser.getFirstName(), userRequestDTO.getFirstName());

        User userDetails = UserMapper.convertToUserEntity(userRequestDTO);
        BeanUtils.copyProperties(userDetails, existingUser, "id", "addresses"); // Exclude ID and addresses for copying

        // Handle addresses:
        existingUser.getAddresses().clear(); // Clear existing addresses (orphanRemoval will delete old ones)
        if (userDetails.getAddresses() != null) {
            userDetails.getAddresses().forEach(newAddress -> {
                newAddress.setUser(existingUser); // Set back-reference
                existingUser.getAddresses().add(newAddress); // Add new/updated addresses
            });
        }

        User updatedUser = userRepository.save(existingUser);
        logger.info("User with ID: {} successfully updated.", updatedUser.getId());
        return UserMapper.convertToUserDTO(updatedUser); // Convert updated entity to DTO
    }

    @Override
    public void deleteUser(Long id) {
        logger.info("Attempting to delete user with ID: {}.", id);
        if (!userRepository.existsById(id)) {
            logger.error("Cannot delete: User with ID: {} does not exist.", id);
            throw new UserNotFoundException("User with ID: " + id + " was not found.");
        }
        userRepository.deleteById(id);
        logger.info("User with ID: {} deleted successfully.", id);
    }

    @Override
    public List<UserResponseDTO> findUsersByAddressCityAndState(String city, String state) { // Removed Pageable
        logger.info("Searching for users with address city '{}' and state '{}'.", city, state);
        List<Address> matchingAddresses = addressRepository.findByCityAndState(city, state);
        return convertAddressesToUniqueUserDTOs(matchingAddresses);
    }

    @Override
    public List<UserResponseDTO> findUsersByAddressCity(String city) { // Removed Pageable
        logger.info("Searching for users with address city '{}'.", city);
        List<Address> matchingAddresses = addressRepository.findByCity(city);
        return convertAddressesToUniqueUserDTOs(matchingAddresses);
    }

    @Override
    public List<UserResponseDTO> findUsersByAddressState(String state) { // Removed Pageable
        logger.info("Searching for users with address state '{}'.", state);
        List<Address> matchingAddresses = addressRepository.findByState(state);
        return convertAddressesToUniqueUserDTOs(matchingAddresses);
    }

    @Override
    public List<UserResponseDTO> findUsersByAddressCityOrState(String city, String state) { // New method
        logger.info("Searching for users with address city '{}' OR state '{}'.", city, state);
        List<Address> matchingAddresses = addressRepository.findByCityOrState(city, state);
        return convertAddressesToUniqueUserDTOs(matchingAddresses);
    }



}
