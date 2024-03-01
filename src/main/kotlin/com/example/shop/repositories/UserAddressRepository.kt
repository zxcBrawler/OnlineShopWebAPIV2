package com.example.shop.repositories


import com.example.shop.models.UserAddress
import org.springframework.data.repository.CrudRepository

interface UserAddressRepository: CrudRepository<UserAddress, Long> {

    fun findAllByUserId (userId : Long) : List<UserAddress>
}