package com.example.shop.repositories


import com.example.shop.models.ClothesSizeClothes
import org.springframework.data.repository.CrudRepository

interface ClothesSizeClothesRepository: CrudRepository<ClothesSizeClothes, Long> {

    fun findAllByClothesIdClothes (id : Long) : List<ClothesSizeClothes>

    fun findByClothesIdClothesAndSizeClothesId (clothesId : Long, sizeId : Long) : ClothesSizeClothes
}