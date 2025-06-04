package com.gevernova.addressbook.service;


import com.gevernova.addressbook.entity.Address;

import java.util.List;
import java.util.Optional;

public interface AddressService {
    List<Address> getAllAddresses();
    Optional<Address> getAddressById(Long id);
    Address createAddress(Address address); // Still accepts/returns Address entity
    Address updateAddress(Long id, Address addressDetails); // Still accepts/returns Address entity
    void deleteAddress(Long id);
}
