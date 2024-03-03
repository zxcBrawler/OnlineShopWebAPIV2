package com.example.shop.repositories

import com.example.shop.models.Address
import org.springframework.data.repository.CrudRepository

interface AddressRepository: CrudRepository<Address, Long>