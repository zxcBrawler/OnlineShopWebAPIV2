package com.example.shop.repositories

import com.example.shop.models.Color
import org.springframework.data.repository.CrudRepository

interface ColorRepository: CrudRepository<Color, Long>