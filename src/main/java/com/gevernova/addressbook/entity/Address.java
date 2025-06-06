package com.gevernova.addressbook.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

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

    @ElementCollection
    @CollectionTable(name = "address_tags",joinColumns=@JoinColumn(name="address_id"))
    @Column(name = "tag_name")
    private List<String> tags;
}
