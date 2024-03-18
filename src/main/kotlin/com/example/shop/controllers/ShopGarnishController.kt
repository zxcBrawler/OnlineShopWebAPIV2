package com.example.shop.controllers

import com.example.shop.models.ShopGarnish
import com.example.shop.models.dto.ShopGarnishDTO
import com.example.shop.repositories.ShopGarnishRepository
import jakarta.websocket.server.PathParam
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@CrossOrigin(maxAge = 3600)
@RestController
@RequestMapping("/api/shopGarnish")
class ShopGarnishController (@Autowired private val shopGarnishRepository: ShopGarnishRepository)  {
    @GetMapping("")
    fun getAllShopGarnish(): List<ShopGarnish> =
        shopGarnishRepository.findAll().toList()

    @PostMapping("")
    fun createShopGarnish(@RequestBody shopGarnish: ShopGarnish): ResponseEntity<ShopGarnish> {
        val createdShopGarnish = shopGarnishRepository.save(shopGarnish)
        return ResponseEntity(createdShopGarnish, HttpStatus.CREATED)
    }

    @GetMapping("/{shopAddressId}")
    fun getAllByShopAddressId(@PathVariable shopAddressId : Long) : ResponseEntity<List<ShopGarnish>>{
        return ResponseEntity(shopGarnishRepository.getAllByShopAddressesGarnishShopAddressId(shopAddressId), HttpStatus.OK)
    }

    @GetMapping("/{colorId}/{sizeId}")
    fun getShopGarnishById(@PathVariable("colorId") colorId: Long, @PathVariable("sizeId") sizeId: Long): ResponseEntity<Any> {
       val existingShopGarnish = shopGarnishRepository.getShopGarnishBySizeClothesGarnishSizeClothesIdAndColorClothesGarnishId(sizeId, colorId)
        return ResponseEntity(existingShopGarnish, HttpStatus.OK)
    }

    @PutMapping("/{shopGarnishId}")
    fun updateQuantity(@PathVariable("shopGarnishId") shopGarnishId : Long, newQuantity : ShopGarnishDTO) : ResponseEntity<ShopGarnish>{
        val existingShopGarnish = shopGarnishRepository.findById(shopGarnishId).orElse(null)

        val updatedShopGarnish = existingShopGarnish.copy(
            quantity = newQuantity.quantity
        )
        shopGarnishRepository.save(updatedShopGarnish)
        return ResponseEntity(updatedShopGarnish, HttpStatus.OK)
    }

    @DeleteMapping("/{id}")
    fun deleteShopGarnishById(@PathVariable("id") shopGarnishId: Long): ResponseEntity<ShopGarnish> {
        if (!shopGarnishRepository.existsById(shopGarnishId)) {
            return ResponseEntity(HttpStatus.NOT_FOUND)
        }
        shopGarnishRepository.deleteById(shopGarnishId)
        return ResponseEntity(HttpStatus.NO_CONTENT)
    }

}