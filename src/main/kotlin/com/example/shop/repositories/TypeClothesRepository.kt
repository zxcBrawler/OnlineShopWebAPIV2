package com.example.shop.repositories


import com.example.shop.models.TypeClothes
import org.springframework.data.repository.CrudRepository

interface TypeClothesRepository: CrudRepository<TypeClothes, Long> {

    fun findAllByCategoryClothesId (categoryId : Long) : List<TypeClothes>
}