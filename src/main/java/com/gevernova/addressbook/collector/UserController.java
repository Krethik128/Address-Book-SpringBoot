package com.gevernova.addressbook.collector;

import com.gevernova.addressbook.dto.UserRequestDTO;
import com.gevernova.addressbook.dto.UserResponseDTO;
import com.gevernova.addressbook.exceptionhandler.UserNotFoundException; // Updated import path
import com.gevernova.addressbook.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/users") // Base path for all user-related endpoints
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    // GET all users
    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
        logger.info("Received request to retrieve all users.");
        List<UserResponseDTO> userResponseDTOs = userService.getAllUsers();
        logger.info("Successfully retrieved {} users.", userResponseDTOs.size());
        return ResponseEntity.ok(userResponseDTOs);
    }

    // GET user by ID
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable Long id) {
        logger.info("Received request to retrieve user with ID: {}", id);
        UserResponseDTO userResponseDTO = userService.getUserById(id)
                .orElseThrow(() -> {
                    logger.warn("User with ID: {} not found.", id);
                    return new UserNotFoundException("User with ID: " + id + " was not found.");
                });
        logger.info("Successfully retrieved user with ID: {}.", id);
        return ResponseEntity.ok(userResponseDTO);
    }

    // POST a new user
    @PostMapping
    public ResponseEntity<UserResponseDTO> createUser(@Valid @RequestBody UserRequestDTO userRequestDTO) {
        logger.info("Received request to create a new user: {} {}.",
                userRequestDTO.getFirstName(), userRequestDTO.getLastName());
        UserResponseDTO createdUser = userService.createUser(userRequestDTO); // Pass DTO directly
        logger.info("Successfully created user with ID: {}.", createdUser.getId());
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }

    // PUT to update an existing user by ID
    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDTO> updateUser(@PathVariable Long id, @Valid @RequestBody UserRequestDTO userRequestDTO) {
        logger.info("Received request to update user with ID: {}.", id);
        UserResponseDTO updatedUser = userService.updateUser(id, userRequestDTO); // Pass DTO directly
        logger.info("Successfully updated user with ID: {}.", updatedUser.getId());
        return ResponseEntity.ok(updatedUser);
    }

    // DELETE a user by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        logger.info("Received request to delete user with ID: {}.", id);
        userService.deleteUser(id);
        logger.info("Successfully deleted user with ID: {}.", id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search-by-address")
    public ResponseEntity<List<UserResponseDTO>> searchUsersByAddressLocation(
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String state) {

        logger.info("Received search request for users by address: city='{}', state='{}'.", city, state);

        boolean hasCity = city != null && !city.isEmpty();
        boolean hasState = state != null && !state.isEmpty();

        List<UserResponseDTO> users;

        if (hasCity && hasState) {
            // Case 1: Both city and state provided (AND condition)
            users = userService.findUsersByAddressCityAndState(city, state);
        } else if (hasCity) {
            // Case 2: Only city provided
            users = userService.findUsersByAddressCity(city);
        } else if (hasState) {
            // Case 3: Only state provided
            users = userService.findUsersByAddressState(state);
        } else {
            // Case 4: Neither city nor state provided - return all users
            logger.info("No specific search parameters provided. Returning all users.");
            users = userService.getAllUsers(); // Or a specific 'findAllAddressesSortedByCity' if you had one.
        }

        logger.info("Returning {} users matching address search criteria.", users.size());
        return ResponseEntity.ok(users);
    }
}