package com.gevernova.addressbook.collector;

import com.gevernova.addressbook.dto.AddressRequestDTO;
import com.gevernova.addressbook.dto.AddressResponseDTO;
import com.gevernova.addressbook.entity.Address;
import com.gevernova.addressbook.entity.AddressNotFoundException;
import com.gevernova.addressbook.service.AddressService;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils; // Utility for copying properties
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/addresses")
public class AddressController {

    private final AddressService addressService;

    @Autowired
    public AddressController(AddressService addressService) {
        this.addressService = addressService;
    }

    // Helper method to convert Entity to Response DTO
    private AddressResponseDTO convertToDto(Address address) {
        AddressResponseDTO addressResponseDTO = new AddressResponseDTO();
        BeanUtils.copyProperties(address, addressResponseDTO); // Copies properties with matching names
        return addressResponseDTO;
    }

    // Helper method to convert Request DTO to Entity
    private Address convertToEntity(AddressRequestDTO addressRequestDTO) {
        Address address = new Address();
        BeanUtils.copyProperties(addressRequestDTO, address);
        return address;
    }

    // Retrieve all addresses
    @GetMapping
    public ResponseEntity<List<AddressResponseDTO>> getAllAddresses() {
        List<Address> addresses = addressService.getAllAddresses();
        List<AddressResponseDTO> addressResponseDTOs = addresses.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(addressResponseDTOs);
    }

    // Retrieve address by identifier
    @GetMapping("/{id}")
    public ResponseEntity<AddressResponseDTO> getAddressById(@PathVariable Long id) {
        Address address = addressService.getAddressById(id)
                .orElseThrow(() -> new AddressNotFoundException("Address with ID: " + id + " was not found."));
        return ResponseEntity.ok(convertToDto(address));
    }

    // Create a new address entry
    @PostMapping
    public ResponseEntity<AddressResponseDTO> createAddress(@Valid @RequestBody AddressRequestDTO addressRequestDTO) {
        Address addressToCreate = convertToEntity(addressRequestDTO);
        Address createdAddress = addressService.createAddress(addressToCreate);
        return new ResponseEntity<>(convertToDto(createdAddress), HttpStatus.CREATED);
    }

    // Update an existing address by identifier
    @PutMapping("/{id}")
    public ResponseEntity<AddressResponseDTO> updateAddress(@PathVariable Long id, @Valid @RequestBody AddressRequestDTO addressRequestDTO) {
        Address addressDetails = convertToEntity(addressRequestDTO);
        // The service layer handles finding the existing entity and updating its fields.
        // We pass the new details as an entity from the DTO.
        Address updatedAddress = addressService.updateAddress(id, addressDetails);
        return ResponseEntity.ok(convertToDto(updatedAddress));
    }

    // Delete an address by identifier
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAddress(@PathVariable Long id) {
        addressService.deleteAddress(id);
        return ResponseEntity.noContent().build();
    }
}