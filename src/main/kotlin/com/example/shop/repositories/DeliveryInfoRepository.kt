package com.example.shop.repositories


import com.example.shop.models.DeliveryInfo
import org.springframework.data.repository.CrudRepository

interface DeliveryInfoRepository : CrudRepository<DeliveryInfo, Long> {

    fun findByOrderIdOrder (orderId : Long) : DeliveryInfo
}