package com.example.shop.models

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*

@Entity
@Table(name = "clothes_sizes_clothes")
data class ClothesSizeClothes(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    var id: Long = 0,

    @ManyToOne
    var clothes: Clothes = Clothes(),

    @ManyToOne
    var sizeClothes: SizeClothes = SizeClothes(),

    @JsonIgnore
    @OneToMany(mappedBy = "sizeClothes")
    var cart : List<Cart> = arrayListOf(),
    @JsonIgnore
    @OneToMany(mappedBy = "sizeClothesGarnish")
    var sizeClothesGarnish : List<ShopGarnish> = arrayListOf()


)
