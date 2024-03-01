package com.example.shop.repositories

import com.example.shop.models.ClothesColors
import com.example.shop.models.ClothesPhoto
import org.springframework.data.repository.CrudRepository

interface ClothesPhotoRepository : CrudRepository<ClothesPhoto, Long> {


}