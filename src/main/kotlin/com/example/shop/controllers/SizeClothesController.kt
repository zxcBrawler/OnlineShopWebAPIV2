package com.example.shop.controllers

import com.example.shop.models.SizeClothes
import com.example.shop.models.dto.SizeDTO
import com.example.shop.repositories.SizeClothesRepository
import com.example.shop.repositories.UserOrderRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@CrossOrigin(maxAge = 3600)
@RestController
@RequestMapping("/api/sizeClothes")
class SizeClothesController(@Autowired private val sizeClothesRepository: SizeClothesRepository)  {
    @GetMapping("")
    fun getAllSizeClothes(): List<SizeClothes> =
        sizeClothesRepository.findAll().toList()

    @PostMapping("")
    fun createSizeClothes(sizeDTO: SizeDTO): ResponseEntity<SizeClothes> {
        val newSize = SizeClothes()
        newSize.nameSize = sizeDTO.nameSize
        val createdSizeClothes = sizeClothesRepository.save(newSize)
        return ResponseEntity(createdSizeClothes, HttpStatus.CREATED)
    }

    @GetMapping("/{id}")
    fun getSizeClothesById(@PathVariable("id") sizeClothesId: Long): ResponseEntity<SizeClothes> {
        val sizeClothes = sizeClothesRepository.findById(sizeClothesId).orElse(null)
        return if (sizeClothes != null) ResponseEntity(sizeClothes, HttpStatus.OK)
        else ResponseEntity(HttpStatus.NOT_FOUND)
    }

    @PutMapping("/{id}")
    fun updateSizeClothesById(@PathVariable("id") sizeClothesId: Long, @RequestBody sizeClothes: SizeClothes): ResponseEntity<SizeClothes> {

        val existingSizeClothes = sizeClothesRepository.findById(sizeClothesId).orElse(null)
            ?: return ResponseEntity(HttpStatus.NOT_FOUND)

        val updatedSizeClothes = existingSizeClothes.copy(
            nameSize = sizeClothes.nameSize)
        sizeClothesRepository.save(updatedSizeClothes)
        return ResponseEntity(updatedSizeClothes, HttpStatus.OK)
    }

    @DeleteMapping("/{id}")
    fun deleteSizeClothesById(@PathVariable("id") sizeClothesId: Long): ResponseEntity<SizeClothes> {
        if (!sizeClothesRepository.existsById(sizeClothesId)) {
            return ResponseEntity(HttpStatus.NOT_FOUND)
        }
        sizeClothesRepository.deleteById(sizeClothesId)
        return ResponseEntity(HttpStatus.NO_CONTENT)
    }

}