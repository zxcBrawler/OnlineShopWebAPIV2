package com.example.shop.repositories

import com.example.shop.models.logs.OrderStatusLog
import org.springframework.data.repository.CrudRepository

interface OrderStatusLogRepository: CrudRepository<OrderStatusLog, Long> {

   fun findAllByOrdersIdOrder (orderId : Long) : List<OrderStatusLog>
}