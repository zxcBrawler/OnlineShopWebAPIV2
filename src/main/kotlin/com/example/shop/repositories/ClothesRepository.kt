package com.example.shop.repositories


import com.example.shop.models.Clothes
import org.springframework.data.repository.CrudRepository

interface ClothesRepository: CrudRepository<Clothes, Long> {

    fun findByTypeClothesIdType (typeId : Long) : List<Clothes>
}