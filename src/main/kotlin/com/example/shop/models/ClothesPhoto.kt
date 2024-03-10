package com.example.shop.models

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*


@Entity
@Table(name = "clothes_photo")
data class ClothesPhoto(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    @Column(length = 500, unique = true)
    var photoAddress: String = "",
    @JsonIgnore
    @OneToMany(mappedBy = "clothesPhoto")
    var clothesPhoto : List<PhotosOfClothes> = arrayListOf()
)
