package com.gevernova.addressbook.repository;

import com.gevernova.addressbook.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository // Designates this interface as a Spring Data JPA repository
public interface AddressRepository extends JpaRepository<Address, Long> {
    // JpaRepository inherently provides methods such as save(), findById(), findAll(), and deleteById().
}
