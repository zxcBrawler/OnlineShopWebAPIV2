package com.example.shop.models.dto

import com.example.shop.models.Order
import com.example.shop.models.StatusOrder

class OrderLogDTO {
    val currentStatus = StatusOrder()
    val order = Order()
    val timestamp = ""
}