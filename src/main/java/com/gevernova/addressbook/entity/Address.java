package com.gevernova.addressbook.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@Data
@Generated
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
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
