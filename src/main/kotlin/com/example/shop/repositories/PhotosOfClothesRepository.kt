package com.example.shop.repositories
import com.example.shop.models.PhotosOfClothes
import org.springframework.data.repository.CrudRepository

interface PhotosOfClothesRepository: CrudRepository<PhotosOfClothes, Long> {

    fun getPhotosOfClothesByClothesIdIdClothes (id : Long) : List<PhotosOfClothes>
}