package com.example.shop.models

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*

@Entity
data class ShopAddresses(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val shopAddressId: Long = 0,
    val shopAddressDirection: String = "", // where shop located exactly
    val shopMetro: String = "", //closest metro station
    val contactNumber: String = "", // contact info
    val latitude: String = "",
    val longitude: String = "",

    @JsonIgnore
    @OneToMany(mappedBy = "shopAddresses")
    var addressList : List<DeliveryInfo> = arrayListOf(),
    @JsonIgnore
    @OneToMany(mappedBy = "shopAddressesGarnish")
    var shopAddressesGarnish : List<ShopGarnish> = arrayListOf(),
    @JsonIgnore
    @OneToMany(mappedBy = "shopAddresses", cascade = [CascadeType.ALL])
    val shopAddresses : List<EmployeeShop> = arrayListOf(),

)
