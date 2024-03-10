package com.example.shop.models

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*

@Entity
@Table(name = "clothes")
data class Clothes(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val idClothes: Long = 0,
    var nameClothesEn: String = "",
    var nameClothesRu: String = "",
    var priceClothes: String = "",
    val clothesPhoto: String = "",
    var barcode: String = "",


    @ManyToOne
    var typeClothes: TypeClothes = TypeClothes(),
    @JsonIgnore
    @OneToMany(mappedBy = "clothes")
    var clothesList : List<ClothesColors> = arrayListOf(),
    @JsonIgnore
    @OneToMany(mappedBy = "clothesId")
    var photoClothesId : List<PhotosOfClothes> = arrayListOf(),
    @JsonIgnore
    @OneToMany(mappedBy = "clothesComp")
    var clothesComp : List<OrderComposition> = arrayListOf(),

    )
