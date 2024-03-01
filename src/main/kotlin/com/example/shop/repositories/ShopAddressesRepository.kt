package com.example.shop.repositories


import com.example.shop.models.ShopAddresses
import org.springframework.data.repository.CrudRepository

interface ShopAddressesRepository: CrudRepository<ShopAddresses, Long> {
}