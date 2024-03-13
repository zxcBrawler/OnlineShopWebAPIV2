package com.example.shop.models

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*

@Entity
@Table(name = "color")
data class Color(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val colorId: Long = 0,
    @Column(unique = true)
    var nameColor: String = "",
    @Column(unique = true)
    var hex: String = "",

    @JsonIgnore
    @OneToMany(mappedBy = "colors")
    var colorsList : List<ClothesColors> = arrayListOf(),

    @JsonIgnore
    @OneToMany(mappedBy = "colorClothes")
    val orderComp : List<OrderComposition> = arrayListOf()
)
