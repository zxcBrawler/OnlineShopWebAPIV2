package com.example.shop.controllers

import com.example.shop.models.ClothesPhoto
import com.example.shop.models.PhotosOfClothes
import com.example.shop.repositories.ClothesPhotoRepository
import com.example.shop.repositories.PhotosOfClothesRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.jpa.repository.Query
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/photosOfClothes")
class PhotosOfClothesController (@Autowired private val photosOfClothesRepository: PhotosOfClothesRepository)  {
    @GetMapping("")
    fun getAllPhotosOfClothes(): List<PhotosOfClothes> =
        photosOfClothesRepository.findAll().toList()

    @PostMapping("")
    fun createPhotosOfClothes(@RequestBody photosOfClothes: PhotosOfClothes): ResponseEntity<PhotosOfClothes> {
        val createdPhotosOfClothes = photosOfClothesRepository.save(photosOfClothes)
        return ResponseEntity(createdPhotosOfClothes, HttpStatus.CREATED)
    }

    @GetMapping("/{clothesId}")
    fun getPhotosOfClothesByClothesId(@PathVariable("clothesId") clothesId: Long): ResponseEntity<Any> {
        val photos = photosOfClothesRepository.getPhotosOfClothesByClothesIdIdClothes(clothesId)
        return ResponseEntity(photos, HttpStatus.OK)
    }

    @DeleteMapping("/{id}")
    fun deletePhotosOfClothesById(@PathVariable("id") photosOfClothesId: Long): ResponseEntity<PhotosOfClothes> {
        if (!photosOfClothesRepository.existsById(photosOfClothesId)) {
            return ResponseEntity(HttpStatus.NOT_FOUND)
        }
        photosOfClothesRepository.deleteById(photosOfClothesId)
        return ResponseEntity(HttpStatus.NO_CONTENT)
    }
}