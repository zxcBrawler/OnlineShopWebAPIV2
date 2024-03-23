package com.example.shop.controllers

import com.example.shop.models.ShopAddresses
import com.example.shop.models.dto.ShopAddressDTO
import com.example.shop.repositories.ShopAddressesRepository

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@CrossOrigin(maxAge = 3600)
@RestController
@RequestMapping("/api/shopAddresses")
class ShopAddressesController (@Autowired private val shopAddressesRepository: ShopAddressesRepository)  {
    @GetMapping("")
    fun getAllShopAddresses(): List<ShopAddresses> =
        shopAddressesRepository.findAll().toList()

    @GetMapping("/{id}")
    fun getShopAddressesById(@PathVariable("id") shopAddressesId: Int): ResponseEntity<ShopAddresses> {
        val shopAddresses = shopAddressesRepository.findById(shopAddressesId.toLong()).orElse(null)
        return ResponseEntity(shopAddresses, HttpStatus.OK)
    }

    //Admin only
    @PostMapping("")
    fun createShopAddresses(shopAddresses: ShopAddressDTO): ResponseEntity<ShopAddresses> {
       val newShopAddress = ShopAddresses()
        newShopAddress.shopMetro = shopAddresses.shopMetro
        newShopAddress.shopAddressDirection = shopAddresses.shopAddressDirection
        newShopAddress.contactNumber = shopAddresses.contactNumber
        newShopAddress.latitude = shopAddresses.latitude
        newShopAddress.longitude = shopAddresses.longitude
        val createdShopAddresses = shopAddressesRepository.save(newShopAddress)
        return ResponseEntity(createdShopAddresses, HttpStatus.CREATED)
    }
    @PutMapping("/{id}")
    fun updateShopAddressesById(@PathVariable("id") shopAddressesId: Long, shopAddresses: ShopAddressDTO): ResponseEntity<ShopAddresses> {

        val existingShopAddresses = shopAddressesRepository.findById(shopAddressesId).orElse(null)
            ?: return ResponseEntity(HttpStatus.NOT_FOUND)

        val updatedShopAddresses = existingShopAddresses.copy(
            shopAddressDirection = shopAddresses.shopAddressDirection,
            shopMetro = shopAddresses.shopMetro,
            contactNumber = shopAddresses.contactNumber,
            latitude = shopAddresses.latitude,
            longitude = shopAddresses.longitude)
        shopAddressesRepository.save(updatedShopAddresses)
        return ResponseEntity(updatedShopAddresses, HttpStatus.OK)
    }

    @DeleteMapping("/{id}")
    fun deleteShopAddressesById(@PathVariable("id") shopAddressesId: Long): ResponseEntity<ShopAddresses> {
        if (!shopAddressesRepository.existsById(shopAddressesId)) {
            return ResponseEntity(HttpStatus.NOT_FOUND)
        }
        shopAddressesRepository.deleteById(shopAddressesId)
        return ResponseEntity(HttpStatus.NO_CONTENT)
    }
    //
}