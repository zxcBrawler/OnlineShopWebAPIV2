package com.example.shop.controllers

import com.example.shop.models.CategoryClothes
import com.example.shop.models.TypeClothes
import com.example.shop.repositories.CategoryClothesRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@CrossOrigin(maxAge = 3600)
@RestController
@RequestMapping("/api/categoryClothes")
class CategoryClothesController (@Autowired private val categoryClothesRepository: CategoryClothesRepository) {
    @GetMapping("")
    fun getAllCategoryClothes(): List<CategoryClothes> =
        categoryClothesRepository.findAll().toList()

    @PostMapping("")
    fun createCategoryClothes(@RequestBody categoryClothes: CategoryClothes): ResponseEntity<CategoryClothes> {
        val createdCategoryClothes = categoryClothesRepository.save(categoryClothes)
        return ResponseEntity(createdCategoryClothes, HttpStatus.CREATED)
    }

    @GetMapping("/{id}")
    fun getCategoryClothesById(@PathVariable("id") categoryClothesId: Long): ResponseEntity<CategoryClothes> {
        val categoryClothes = categoryClothesRepository.findById(categoryClothesId).orElse(null)
        return if (categoryClothes != null) ResponseEntity(categoryClothes, HttpStatus.OK)
        else ResponseEntity(HttpStatus.NOT_FOUND)
    }

    @PutMapping("/{id}")
    fun updateCategoryClothesById(@PathVariable("id") categoryClothesId: Long, @RequestBody categoryClothes: CategoryClothes): ResponseEntity<CategoryClothes> {

        val existingCategoryClothes = categoryClothesRepository.findById(categoryClothesId).orElse(null)
            ?: return ResponseEntity(HttpStatus.NOT_FOUND)

        val updatedCategoryClothes = existingCategoryClothes.copy(
           nameCategory = categoryClothes.nameCategory)
        categoryClothesRepository.save(updatedCategoryClothes)
        return ResponseEntity(updatedCategoryClothes, HttpStatus.OK)
    }

    @DeleteMapping("/{id}")
    fun deleteCategoryClothesById(@PathVariable("id") categoryClothesId: Long): ResponseEntity<CategoryClothes> {
        if (!categoryClothesRepository.existsById(categoryClothesId)) {
            return ResponseEntity(HttpStatus.NOT_FOUND)
        }
        categoryClothesRepository.deleteById(categoryClothesId)
        return ResponseEntity(HttpStatus.NO_CONTENT)
    }
}