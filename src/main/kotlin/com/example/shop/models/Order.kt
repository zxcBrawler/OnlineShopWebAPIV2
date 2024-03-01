package com.example.shop.models

import com.example.shop.models.logs.OrderStatusLog
import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*

@Entity
@Table(name = "orders")
data class Order(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val idOrder: Long = 0,
    @Column(unique = true)
    var numberOrder: String = "",
    var timeOrder: String = "",
    var dateOrder: String = "",
    var sumOrder: String = "",

    @ManyToOne
    var currentStatus: StatusOrder = StatusOrder(),

    @JsonIgnore
    @OneToMany(mappedBy = "orderId", cascade = [CascadeType.ALL])
    var orderId : List<OrderComposition> = arrayListOf(),

    @JsonIgnore
    @OneToMany(mappedBy = "orders", cascade = [CascadeType.ALL])
    val userOrder : List<UserOrder> = arrayListOf(),

    @JsonIgnore
    @OneToMany(mappedBy = "order", cascade = [CascadeType.ALL])
    val deliveryInfo : List<DeliveryInfo> = arrayListOf(),

    @JsonIgnore
    @OneToMany(mappedBy = "orders", cascade = [CascadeType.ALL])
    val orderStatusLogOrder : List<OrderStatusLog> = arrayListOf(),

    @ManyToOne(cascade = [CascadeType.ALL])
    var userCard: UserCard = UserCard(),
)
