package com.example.shop.repositories


import com.example.shop.models.ClothesColors
import org.springframework.data.repository.CrudRepository

interface ClothesColorsRepository: CrudRepository<ClothesColors, Long> {

    fun findAllByClothesIdClothes (id : Long) : List<ClothesColors>
}