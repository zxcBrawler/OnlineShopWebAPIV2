package com.example.shop.controllers

import com.example.shop.models.DeliveryInfo
import com.example.shop.models.TypeDelivery
import com.example.shop.repositories.DeliveryInfoRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*


@CrossOrigin(maxAge = 3600)
@RestController
@RequestMapping("/api/deliveryInfo")
class DeliveryInfoController (@Autowired private val deliveryInfoRepository: DeliveryInfoRepository) {
    @GetMapping("")
    fun getAllDeliveryInfo(): List<DeliveryInfo> =
        deliveryInfoRepository.findAll().toList()

    @GetMapping("/{orderId}")
    fun getDeliveryInfoByOrderId(@PathVariable("orderId") id : Long) : ResponseEntity<DeliveryInfo>{
        return ResponseEntity(deliveryInfoRepository.findByOrderIdOrder(id),HttpStatus.OK)
    }


    @GetMapping("/info/{id}")
    fun getTypeDeliveryById(@PathVariable("id") deliveryInfoId: Long): ResponseEntity<DeliveryInfo> {
        val deliveryInfo = deliveryInfoRepository.findById(deliveryInfoId).orElse(null)
        return if (deliveryInfo != null) ResponseEntity(deliveryInfo, HttpStatus.OK)
        else ResponseEntity(HttpStatus.NOT_FOUND)
    }
}