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
@EqualsAndHashCode(exclude = "user")
public class Address {

    @Id // Identifies this field as the primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Configures the primary key for auto-incrementation
    private Long id;

    @Column(name = "address_line1")
    private String street;

    @Column(name = "address_line2")
    private String addressLine2;

    private String city;

    private String state;

    private String zipCode;

    private String country;

    @ElementCollection
    @CollectionTable(name = "address_tags",joinColumns=@JoinColumn(name="address_id"))
    @Column(name = "tag_name")
    private List<String> tags;

    @ManyToOne
    @JoinColumn(name = "user_id" ,nullable = false)
    private User user;
}