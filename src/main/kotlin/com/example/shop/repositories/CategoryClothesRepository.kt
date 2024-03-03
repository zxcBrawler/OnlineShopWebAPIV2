package com.example.shop.repositories

import com.example.shop.models.CategoryClothes
import org.springframework.data.repository.CrudRepository

interface CategoryClothesRepository : CrudRepository<CategoryClothes, Long>