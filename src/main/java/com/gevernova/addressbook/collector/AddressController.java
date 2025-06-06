package com.gevernova.addressbook.collector;

import com.gevernova.addressbook.dto.AddressDataDTO;
import com.gevernova.addressbook.dto.AddressRequestDTO;
import com.gevernova.addressbook.dto.AddressResponseDTO;
import com.gevernova.addressbook.entity.Address;
import com.gevernova.addressbook.exceptionhandler.AddressNotFoundException;
import com.gevernova.addressbook.service.AddressService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils; // Utility for copying properties
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController // Marks this class as a REST controller
@RequestMapping("/api/addresses") // Base path for all endpoints in this controller
@RequiredArgsConstructor
public class AddressController {

    private static final Logger logger = LoggerFactory.getLogger(AddressController.class);

    private final AddressService addressService;

    private AddressDataDTO convertToDataDto(Address address) {
        if (address == null) {
            return null;
        }
        logger.debug("Converting Address entity with ID {} to AddressDataDTO.", address.getId());
        AddressDataDTO addressDataDTO = new AddressDataDTO();
        BeanUtils.copyProperties(address, addressDataDTO);
        return addressDataDTO;
    }


    // Helper method to convert Request DTO to Entity
    private Address convertToEntity(AddressRequestDTO addressRequestDTO) {
        logger.debug("Converting AddressRequestDTO to Address entity.");
        Address address = new Address();
        BeanUtils.copyProperties(addressRequestDTO, address);
        return address;
    }

    // GET all addresses
    @GetMapping // Maps GET requests to /api/addresses
    public ResponseEntity<AddressResponseDTO> getAllAddresses() {
        logger.info("Received request to retrieve all addresses.");
        List<Address> addresses = addressService.getAllAddresses();
        List<AddressDataDTO> addressDataDTOs = addresses.stream()
                .map(this::convertToDataDto) // Use the new convertToDataDto helper
                .toList();
        logger.info("Successfully retrieved {} addresses.", addresses.size());
        AddressResponseDTO response = AddressResponseDTO.builder()
                .message("Successfully retrieved all addresses.")
                .data(addressDataDTOs)
                .build();
        return ResponseEntity.ok(response); // Returns 200 OK with the list of addresses
    }

    // GET address by ID
    @GetMapping("/{id}") // Maps GET requests to /api/addresses/{id}
    public ResponseEntity<AddressResponseDTO> getAddressById(@PathVariable Long id) {
        logger.info("Received request to retrieve address with ID: {}", id);
        Address address = addressService.getAddressById(id)
                .orElseThrow(() -> {
                    logger.warn("Address with ID: {} not found.", id);
                    return new AddressNotFoundException("Address with ID: " + id + " was not found.");
                });
        logger.info("Successfully retrieved address with ID: {}.", id);
        AddressResponseDTO response =AddressResponseDTO.builder()
                .message("Successfully retrieved address.")
                .data(convertToDataDto(address))
                .build();
        return ResponseEntity.ok(response); // Returns 200 OK with the found address
    }

    // POST a new address
    @PostMapping // Maps POST requests to /api/addresses
    public ResponseEntity<AddressResponseDTO> createAddress(@Valid @RequestBody AddressRequestDTO addressRequestDTO) {
        logger.info("Received request to create a new address for: {} {}",
                addressRequestDTO.getFirstName(), addressRequestDTO.getLastName());
        Address addressToCreate = convertToEntity(addressRequestDTO);
        Address createdAddress = addressService.createAddress(addressToCreate);
        logger.info("Successfully created address with ID: {}.", createdAddress.getId());
        AddressResponseDTO response = AddressResponseDTO.builder()
                .message("Address created successfully!")
                .data(convertToDataDto(createdAddress))
                .build();
        return new ResponseEntity<>(response, HttpStatus.CREATED); // Returns 201 Created with the new address
    }

    // PUT to update an existing address by ID
    @PutMapping("/{id}") // Maps PUT requests to /api/addresses/{id}
    public ResponseEntity<AddressResponseDTO> updateAddress(@PathVariable Long id, @Valid @RequestBody AddressRequestDTO addressRequestDTO) {
        logger.info("Received request to update address with ID: {}.", id);
        Address addressDetails = convertToEntity(addressRequestDTO); // Convert DTO to entity for update logic
        Address updatedAddress = addressService.updateAddress(id, addressDetails);
        logger.info("Successfully updated address with ID: {}. ", updatedAddress.getId()); // Removed the dot for consistency
        AddressResponseDTO response = AddressResponseDTO.builder()
                .message("Address updated successfully!")
                .data(convertToDataDto(updatedAddress))
                .build();
        return ResponseEntity.ok(response); // Returns 200 OK with the updated address
    }

    // DELETE an address by ID
    @DeleteMapping("/{id}") // Maps DELETE requests to /api/addresses/{id}
    public ResponseEntity<AddressResponseDTO> deleteAddress(@PathVariable Long id) {
        logger.info("Received request to delete address with ID: {}.", id);
        addressService.deleteAddress(id);
        logger.info("Successfully deleted address with ID: {}.", id);
        // Wrap a success message (data can be null or a confirmation string)
        AddressResponseDTO response = AddressResponseDTO.builder()
                .message("Address deleted successfully!")
                .data("Address with ID " + id + " has been removed.")
                .build();
        return ResponseEntity.ok(response);// Returns 204 No Content
    }
}