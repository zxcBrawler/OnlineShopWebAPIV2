package com.example.shop.models

import jakarta.persistence.*

@Entity
@Table(name = "user_order")
data class UserOrder(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val userOrderId: Long = 0,

    @ManyToOne(cascade = [CascadeType.ALL])
    var user: User = User(),

    @ManyToOne(cascade = [CascadeType.ALL])
    var orders: Order = Order(),
)
