package com.example.shop.controllers

import com.example.shop.models.Clothes
import com.example.shop.repositories.ClothesRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@CrossOrigin(maxAge = 3600)
@RestController
@RequestMapping("/api/clothes")
class ClothesController(@Autowired private val clothesRepository: ClothesRepository)  {
    @GetMapping("")
    fun getAllClothes(): List<Clothes> =
        clothesRepository.findAll().toList()

    @PostMapping("")
    fun createClothes(@RequestBody clothes: Clothes): ResponseEntity<Clothes> {
        val createdClothes = clothesRepository.save(clothes)
        return ResponseEntity(createdClothes, HttpStatus.CREATED)
    }

    @GetMapping("/{id}")
    fun getClothesById(@PathVariable("id") clothesId: Long): ResponseEntity<Clothes> {
        val clothes = clothesRepository.findById(clothesId).orElse(null)
        return if (clothes != null) ResponseEntity(clothes, HttpStatus.OK)
        else ResponseEntity(HttpStatus.NOT_FOUND)
    }

    @GetMapping("/type/{id}")
    fun getClothesByTypeId(@PathVariable("id") typeId: Long): ResponseEntity<List<Clothes>> {
        val clothes = clothesRepository.findByTypeClothesIdType(typeId)
        return ResponseEntity(clothes, HttpStatus.OK)
    }

    @PutMapping("/{id}")
    fun updateClothesById(@PathVariable("id") clothesId: Long, @RequestBody clothes: Clothes): ResponseEntity<Clothes> {

        val existingClothes = clothesRepository.findById(clothesId).orElse(null)
            ?: return ResponseEntity(HttpStatus.NOT_FOUND)

        val updatedClothes = existingClothes.copy(
            nameClothesEn = clothes.nameClothesEn,
            nameClothesRu = clothes.nameClothesRu,
            priceClothes = clothes.priceClothes,
            barcode = clothes.barcode,
            typeClothes = clothes.typeClothes)
        clothesRepository.save(updatedClothes)
        return ResponseEntity(updatedClothes, HttpStatus.OK)
    }

    @DeleteMapping("/{id}")
    fun deleteClothesById(@PathVariable("id") clothesId: Long): ResponseEntity<Clothes> {
        if (!clothesRepository.existsById(clothesId)) {
            return ResponseEntity(HttpStatus.NOT_FOUND)
        }
        clothesRepository.deleteById(clothesId)
        return ResponseEntity(HttpStatus.NO_CONTENT)
    }

}