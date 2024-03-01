package com.example.shop.repositories


import com.example.shop.models.OrderComposition
import org.springframework.data.repository.CrudRepository

interface OrderCompositionRepository: CrudRepository<OrderComposition, Long> {

    fun findByOrderIdIdOrder (orderId : Long) : List<OrderComposition>
}