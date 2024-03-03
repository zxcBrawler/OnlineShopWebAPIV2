package com.example.shop.repositories

import com.example.shop.models.Card
import org.springframework.data.repository.CrudRepository

interface CardRepository: CrudRepository<Card, Long>