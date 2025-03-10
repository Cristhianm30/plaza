package com.pragma.powerup.infrastructure.out.jpa.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "restaurants")
public class RestaurantEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String nit;  // Requerido según tu código actual

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private String phone;

    @Column(nullable = false)
    private String logoUrl;

    @Column(nullable = false)
    private Long ownerId;

    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL)
    private List<DishEntity> dishes = new ArrayList<>();

    @OneToMany(mappedBy = "restaurant")
    private List<OrderEntity> orders = new ArrayList<>();




}
