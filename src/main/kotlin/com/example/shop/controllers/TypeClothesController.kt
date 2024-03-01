package com.example.shop.controllers

import com.example.shop.models.TypeClothes
import com.example.shop.models.UserOrder
import com.example.shop.repositories.TypeClothesRepository
import com.example.shop.repositories.UserOrderRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/api/typeClothes")
class TypeClothesController(@Autowired private val typeClothesRepository: TypeClothesRepository) {
    @GetMapping("")
    fun getAllTypesClothes(): List<TypeClothes> =
        typeClothesRepository.findAll().toList()

    @GetMapping("/{id}")
    fun getTypeClothesByCategoryClothesId(@PathVariable("id") categoryId : Long) : ResponseEntity<List<TypeClothes>> =
        ResponseEntity(typeClothesRepository.findAllByCategoryClothesId(categoryId), HttpStatus.OK  )


    @PostMapping("")
    fun createTypeClothes(@RequestBody typeClothes: TypeClothes): ResponseEntity<TypeClothes> {
        val createdTypeClothes = typeClothesRepository.save(typeClothes)
        return ResponseEntity(createdTypeClothes, HttpStatus.CREATED)
    }

    @PutMapping("/{id}")
    fun updateTypeClothesById(@PathVariable("id") typeClothesId: Long, @RequestBody typeClothes: TypeClothes): ResponseEntity<TypeClothes> {

        val existingTypeClothes = typeClothesRepository.findById(typeClothesId).orElse(null)
            ?: return ResponseEntity(HttpStatus.NOT_FOUND)

        val updatedTypeClothes = existingTypeClothes.copy(
            nameType = typeClothes.nameType,
            categoryClothes = typeClothes.categoryClothes)
        typeClothesRepository.save(updatedTypeClothes)
        return ResponseEntity(updatedTypeClothes, HttpStatus.OK)
    }

    @DeleteMapping("/{id}")
    fun deleteTypeClothesById(@PathVariable("id") typeClothesId: Long): ResponseEntity<TypeClothes> {
        if (!typeClothesRepository.existsById(typeClothesId)) {
            return ResponseEntity(HttpStatus.NOT_FOUND)
        }
        typeClothesRepository.deleteById(typeClothesId)
        return ResponseEntity(HttpStatus.NO_CONTENT)
    }

}