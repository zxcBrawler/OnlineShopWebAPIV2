package com.example.shop.models

import jakarta.persistence.*

@Entity
@Table(name = "employee_shop")
data class EmployeeShop (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0,

    @ManyToOne
    var employee : User = User(),
    @ManyToOne
    var shopAddresses : ShopAddresses = ShopAddresses(),
)