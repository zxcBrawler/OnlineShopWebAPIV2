package com.example.shop.controllers

import com.example.shop.models.ClothesColors
import com.example.shop.repositories.ClothesColorsRepository
import com.example.shop.repositories.ClothesRepository

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@CrossOrigin(maxAge = 3600)
@RestController
@RequestMapping("/api/clothesColors")
class ClothesColorsController (
    @Autowired private val clothesColorsRepository: ClothesColorsRepository)  {
    @GetMapping("")
    fun getAllClothesColors(): List<ClothesColors> =
        clothesColorsRepository.findAll().toList()

    @PostMapping("")
    fun createClothesColors(@RequestBody clothesColors: ClothesColors): ResponseEntity<ClothesColors> {
        val createdClothesColors = clothesColorsRepository.save(clothesColors)
        return ResponseEntity(createdClothesColors, HttpStatus.CREATED)
    }

    @GetMapping("/{clothesId}")
    fun getClothesColorsByClothesId(@PathVariable("clothesId") clothesId: Long): ResponseEntity<Any> {
        val clothesColor = clothesColorsRepository.findAllByClothesIdClothes(clothesId)
        return ResponseEntity(clothesColor, HttpStatus.OK)
    }



    @DeleteMapping("/{id}")
    fun deleteClothesColorsById(@PathVariable("id") clothesColorsId: Long): ResponseEntity<ClothesColors> {
        if (!clothesColorsRepository.existsById(clothesColorsId)) {
            return ResponseEntity(HttpStatus.NOT_FOUND)
        }
        clothesColorsRepository.deleteById(clothesColorsId)
        return ResponseEntity(HttpStatus.NO_CONTENT)
    }

}