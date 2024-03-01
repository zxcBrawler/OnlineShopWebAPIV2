package com.example.shop.repositories

import com.example.shop.models.Cart
import org.springframework.data.repository.CrudRepository

interface CartRepository: CrudRepository<Cart, Long> {

    fun findByUserId(userId : Long) : List<Cart>

    fun findBySizeClothesSizeClothesIdAndColorClothesCartColorsColorIdAndSizeClothesClothesIdClothesAndUserId
                (sizeId: Long?, colorId : Long?, clothesId : Long?, userId : Long?) : Cart?

    fun deleteAllByUserId (userId: Long) : Any
}