package com.example.shop.models

import jakarta.persistence.*

@Entity
@Table(name = "shop_clothes")
data class ShopGarnish(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val shopGarnishId: Long = 0,

    //DONE
    @ManyToOne
    var sizeClothesGarnish: ClothesSizeClothes = ClothesSizeClothes(),

    //DONE
    @ManyToOne
    var colorClothesGarnish: ClothesColors = ClothesColors(),

    //DONE
    @ManyToOne
    var shopAddressesGarnish: ShopAddresses = ShopAddresses(),

    var quantity: Int = 0,
)
