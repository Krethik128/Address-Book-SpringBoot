package com.gevernova.addressbook.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity // Designates this class as a JPA entity
@Data // Lombok annotation for automatic generation of getters, setters, toString, equals, and hashCode methods
@NoArgsConstructor // Lombok annotation for the generation of a no-argument constructor
@AllArgsConstructor // Lombok annotation for the generation of an all-argument constructor
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
