package com.example.shop.models

import jakarta.persistence.*

@Entity
@Table(name = "photos_of_clothes")
data class PhotosOfClothes (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    //DONE
    @ManyToOne
    var clothesId: Clothes = Clothes(),
    //DONE
    @ManyToOne
    var clothesPhoto: ClothesPhoto = ClothesPhoto(),
)