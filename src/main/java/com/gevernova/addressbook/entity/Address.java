package com.gevernova.addressbook.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Address {

    @Id // Identifies this field as the primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Configures the primary key for auto-incrementation
    private Long id;

    private String firstName;

    private String lastName;

    private String phoneNumber;

    private String email;

    private String addressLine1;

    private String addressLine2;

    private String city;

    private String state;

    private String zipCode;

    private String country;
}
