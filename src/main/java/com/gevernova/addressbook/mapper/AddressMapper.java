package com.gevernova.addressbook.mapper;

import com.gevernova.addressbook.dto.AddressResponseDTO;
import com.gevernova.addressbook.dto.AddressRequestDTO;
import com.gevernova.addressbook.entity.Address;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;

public class AddressMapper {
    private static final Logger logger = LoggerFactory.getLogger(AddressMapper.class);

    public static AddressResponseDTO convertToAddressToDTO(Address address) {
        logger.debug("Converting Address entity with ID {} to AddressResponseDTO.", address.getId());
        AddressResponseDTO addressResponseDTO = new AddressResponseDTO();
        BeanUtils.copyProperties(address, addressResponseDTO, "user"); // Exclude 'user' to prevent circular dependencies
        return addressResponseDTO;
    }


    // Converts AddressRequestDTO to Address entity
    public static Address convertToAddressEntity(AddressRequestDTO addressRequestDTO) {
        logger.debug("Converting AddressRequestDTO to Address entity.");
        Address address = new Address();
        BeanUtils.copyProperties(addressRequestDTO, address);
        return address;
    }
}
