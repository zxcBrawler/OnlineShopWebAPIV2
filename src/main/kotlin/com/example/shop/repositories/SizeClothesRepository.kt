package com.example.shop.repositories


import com.example.shop.models.SizeClothes
import org.springframework.data.repository.CrudRepository

interface SizeClothesRepository: CrudRepository<SizeClothes, Long>