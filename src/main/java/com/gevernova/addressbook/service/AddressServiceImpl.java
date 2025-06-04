package com.gevernova.addressbook.service;

import com.gevernova.addressbook.entity.Address;
import com.gevernova.addressbook.entity.AddressNotFoundException;
import com.gevernova.addressbook.repository.AddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepository;

    @Autowired
    public AddressServiceImpl(AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }

    @Override
    public List<Address> getAllAddresses() {
        return addressRepository.findAll();
    }

    @Override
    public Optional<Address> getAddressById(Long id) {
        return addressRepository.findById(id);
    }

    @Override
    public Address createAddress(Address address) {
        return addressRepository.save(address);
    }

    @Override
    public Address updateAddress(Long id, Address addressDetails) {
        Address address = addressRepository.findById(id)
                .orElseThrow(() -> new AddressNotFoundException("Address with ID: " + id + " was not found."));

        // Update pertinent fields from the addressDetails entity received from the controller
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

        return addressRepository.save(address);
    }

    @Override
    public void deleteAddress(Long id) {
        if (!addressRepository.existsById(id)) {
            throw new AddressNotFoundException("Address with ID: " + id + " was not found.");
        }
        addressRepository.deleteById(id);
    }
}
