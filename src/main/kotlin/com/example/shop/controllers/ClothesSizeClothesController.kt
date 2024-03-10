package com.example.shop.controllers

import com.example.shop.models.ClothesSizeClothes
import com.example.shop.models.SizeClothes
import com.example.shop.repositories.ClothesSizeClothesRepository
import com.example.shop.repositories.SizeClothesRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*


@CrossOrigin(maxAge = 3600)
@RestController
@RequestMapping("/api/clothesSizeClothes")
class ClothesSizeClothesController (@Autowired private val clothesSizeClothesRepository: ClothesSizeClothesRepository)  {
    @GetMapping("")
    fun getAllClothesSizeClothes(): List<ClothesSizeClothes> =
        clothesSizeClothesRepository.findAll().toList()

    @PostMapping("")
    fun createClothesSizeClothes(@RequestBody sizeClothes: ClothesSizeClothes): ResponseEntity<ClothesSizeClothes> {
        val createdClothesSizeClothes = clothesSizeClothesRepository.save(sizeClothes)
        return ResponseEntity(createdClothesSizeClothes, HttpStatus.CREATED)
    }

    @GetMapping("/{clothesId}")
    fun getClothesSizeClothesByClothesId(@PathVariable("clothesId") clothesId: Long): ResponseEntity<Any> {
        val sizeClothes = clothesSizeClothesRepository.findAllByClothesIdClothes(clothesId)
        return ResponseEntity(sizeClothes, HttpStatus.OK)
    }

    @DeleteMapping("/{id}")
    fun deleteClothesSizeClothesById(@PathVariable("id") clothesSizeClothesId: Long): ResponseEntity<ClothesSizeClothes> {
        if (!clothesSizeClothesRepository.existsById(clothesSizeClothesId)) {
            return ResponseEntity(HttpStatus.NOT_FOUND)
        }
        clothesSizeClothesRepository.deleteById(clothesSizeClothesId)
        return ResponseEntity(HttpStatus.NO_CONTENT)
    }

}