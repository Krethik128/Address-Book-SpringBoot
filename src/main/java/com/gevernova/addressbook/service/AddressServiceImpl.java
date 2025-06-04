package com.gevernova.addressbook.service;

import com.gevernova.addressbook.entity.Address;
import com.gevernova.addressbook.entity.AddressNotFoundException;
import com.gevernova.addressbook.repository.AddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepository;
    private static final Logger logger = LoggerFactory.getLogger(AddressServiceImpl.class);

    @Autowired
    public AddressServiceImpl(AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }

    @Override
    public List<Address> getAllAddresses() {
        logger.debug("Fetching all addresses from the repository.");
        List<Address>addresses = addressRepository.findAll();
        logger.debug("Found {} addresses.", addresses.size());
        return addressRepository.findAll();
    }

    @Override
    public Optional<Address> getAddressById(Long id) {
        logger.debug("Attempting to find address with ID: {}.", id);
        Optional<Address> address = addressRepository.findById(id);
        if (address.isPresent()) {
            logger.debug("Address with ID: {} found.", id);
        } else {
            logger.debug("Address with ID: {} not found in repository.", id);
        }
        return address;
    }

    @Override
    public Address createAddress(Address address) {
        logger.info("Saving new address for {} {}.", address.getFirstName(), address.getLastName());
        Address savedAddress = addressRepository.save(address);
        logger.info("New address saved with ID: {}.", savedAddress.getId());
        return savedAddress;
    }

    @Override
    public Address updateAddress(Long id, Address addressDetails) {
        logger.info("Updating address with ID: {}.", id);
        Address address = addressRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Attempted to update non-existent address with ID: {}.", id);
                    return new AddressNotFoundException("Address with ID: " + id + " was not found.");
                });

        // Update fields
        logger.debug("Updating fields for address ID {}. Old first name: {}, New first name: {}", id, address.getFirstName(), addressDetails.getFirstName());
        address.setFirstName(addressDetails.getFirstName());
        address.setLastName(addressDetails.getLastName());
        address.setPhoneNumber(addressDetails.getPhoneNumber());
        address.setEmail(addressDetails.getEmail());
        address.setAddressLine1(addressDetails.getAddressLine1());
        address.setAddressLine2(addressDetails.getAddressLine2());
        address.setCity(addressDetails.getCity());
        address.setState(addressDetails.getState());
        address.setZipCode(addressDetails.getZipCode());
        address.setCountry(addressDetails.getCountry());

        Address updatedAddress = addressRepository.save(address);
        logger.info("Address with ID: {} successfully updated.", updatedAddress.getId());
        return updatedAddress;
    }

    @Override
    public void deleteAddress(Long id) {
        logger.info("Attempting to delete address with ID: {}.", id);
        if (!addressRepository.existsById(id)) {
            logger.error("Cannot delete: Address with ID: {} does not exist.", id);
            throw new AddressNotFoundException("Address with ID: " + id + " was not found.");
        }
        addressRepository.deleteById(id);
        logger.info("Address with ID: {} deleted successfully.", id);

    }
}
