package com.gevernova.addressbook.repository;

import com.gevernova.addressbook.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository // Designates this interface as a Spring Data JPA repository
public interface AddressRepository extends JpaRepository<Address, Long> {
    // JpaRepository inherently provides methods such as save(), findById(), findAll(), and deleteById().

    @Query(value = "SELECT * FROM address WHERE LOWER(city) LIKE LOWER(CONCAT('%', :city, '%'))", nativeQuery = true)
    List<Address> findByCity(@Param("city") String city);

    @Query(value = "SELECT * FROM address WHERE LOWER(state) LIKE LOWER(CONCAT('%', :state, '%'))", nativeQuery = true)
    List<Address> findByState(@Param("state") String state);

    @Query(value = "SELECT * FROM address WHERE LOWER(city) LIKE LOWER(CONCAT('%', :city, '%')) AND LOWER(state) LIKE LOWER(CONCAT('%', :state, '%'))", nativeQuery = true)
    List<Address> findByCityAndState(@Param("city") String city, @Param("state") String state);

    @Query(value = "SELECT * FROM address WHERE LOWER(city) LIKE LOWER(CONCAT('%', :city, '%')) OR LOWER(state) LIKE LOWER(CONCAT('%', :state, '%'))", nativeQuery = true)
    List<Address> findByCityOrState(@Param("city") String city, @Param("state") String state);

    @Query(value = "SELECT * FROM address ORDER BY city ASC", nativeQuery = true)
    List<Address> findAllSortedByCityNative();

}
