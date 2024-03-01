package com.example.shop.repositories

import com.example.shop.models.UserOrder
import org.springframework.data.repository.CrudRepository

interface UserOrderRepository: CrudRepository<UserOrder, Long> {
    fun findAllByUserId (userId : Long) : List<UserOrder>?
}