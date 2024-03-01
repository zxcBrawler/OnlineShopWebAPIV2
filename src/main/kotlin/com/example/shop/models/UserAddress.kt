package com.example.shop.models

import com.example.shop.models.Address
import jakarta.persistence.*

@Entity
@Table(name = "user_address")
data class UserAddress(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val userAddressId: Long = 0,

    //DONE
    @ManyToOne(cascade = [CascadeType.ALL])
    var user: User = User(),

    //DONE
    @ManyToOne(cascade = [CascadeType.ALL])
    var address: Address = Address(),
)
