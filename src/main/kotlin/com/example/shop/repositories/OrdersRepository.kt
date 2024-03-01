package com.example.shop.repositories

import com.example.shop.models.Order
import org.springframework.data.repository.CrudRepository

interface OrdersRepository: CrudRepository<Order, Long> {


}