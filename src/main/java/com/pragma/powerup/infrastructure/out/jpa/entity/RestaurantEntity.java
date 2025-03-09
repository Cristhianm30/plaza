package com.pragma.powerup.infrastructure.out.jpa.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
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

    @Column(name = "name",nullable = false )
    private String name;

    @Column(name = "address", nullable = false)
    private String address;

    @Column(name = "owner_id", nullable = false)
    private Long ownerId;

    @Column(name = "cell_phone", nullable = false)
    private String cellPhone;

    @Column(name = "url_logo", nullable = false)
    private String urlLogo;

    @Column(name = "nit", nullable = false)
    private String nit;

    @OneToMany(mappedBy = "restaurant")
    private List<DishEntity> dishList;

    @OneToMany(mappedBy = "restaurant")
    private List<OrderEntity> orderList;




}
