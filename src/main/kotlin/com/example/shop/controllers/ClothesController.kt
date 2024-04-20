package com.example.shop.controllers

import com.example.shop.models.*
import com.example.shop.models.dto.ClothesDTO
import com.example.shop.repositories.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@CrossOrigin(maxAge = 3600)
@RestController
@RequestMapping("/api/clothes")
class ClothesController(@Autowired private val clothesRepository: ClothesRepository,
    @Autowired private val clothesPhotoRepository: ClothesPhotoRepository,
    @Autowired private val photosOfClothesRepository: PhotosOfClothesRepository,
    @Autowired private val clothesSizeClothesRepository: ClothesSizeClothesRepository,
    @Autowired private val clothesColorsRepository: ClothesColorsRepository,
    @Autowired private val typeClothesRepository: TypeClothesRepository,
    @Autowired private val colorsRepository: ColorRepository,
    @Autowired private val sizeClothesRepository: SizeClothesRepository)  {
    @GetMapping("")
    fun getAllClothes(): List<Clothes> =
        clothesRepository.findAll().toList()

    /**
     * Handles the creation of a new clothes item based on the provided ClothesDTO.
     *
     * @param clothesDTO The data transfer object containing information for the new clothes item.
     * @return ResponseEntity containing the newly created clothes item and HTTP status code.
     */
    @PostMapping("")
    fun createClothes(clothesDTO: ClothesDTO): ResponseEntity<Any> {
        // Create instances for various components of the clothes item
        val newClothes = Clothes()
        var newPhoto = ClothesPhoto()
        var newClothesPhoto = PhotosOfClothes()
        var newColor = ClothesColors()
        var newSize = ClothesSizeClothes()

        // Retrieve the existing type of clothes from the repository based on the selected type ID
        val existingTypeClothes = typeClothesRepository.findById(clothesDTO.selectedTypeClothes.toLong()).orElse(null)

        // Set basic information for the new clothes item
        newClothes.barcode = clothesDTO.barcode
        newClothes.priceClothes = clothesDTO.priceClothes
        newClothes.nameClothesEn = clothesDTO.nameClothesEn
        newClothes.nameClothesRu = clothesDTO.nameClothesRu
        newClothes.typeClothes = existingTypeClothes

        // Save the new clothes item to the repository
        clothesRepository.save(newClothes)

        // Process and save uploaded photos for the new clothes item
        for (item in clothesDTO.uploadedPhotos) {
            newPhoto.photoAddress = item
            clothesPhotoRepository.save(newPhoto)
            val addedPhoto = clothesPhotoRepository.findById(newPhoto.id).orElse(null)
            newClothesPhoto.clothesId = newClothes
            newClothesPhoto.clothesPhoto = addedPhoto
            photosOfClothesRepository.save(newClothesPhoto)
            newPhoto = ClothesPhoto()
            newClothesPhoto = PhotosOfClothes()

            // Update the clothes item's main photo if it is empty
            // Should only add once when first photo is added
            if (newClothes.clothesPhoto.isEmpty()) {
                val updatedClothes = newClothes.copy(clothesPhoto = item)
                clothesRepository.save(updatedClothes)
            }
        }

        // Process and save selected sizes for the new clothes item
        for (item in clothesDTO.selectedSizes) {
            val addedSize = sizeClothesRepository.findById(item.toLong()).orElse(null)
            newSize.sizeClothes = addedSize
            newSize.clothes = newClothes
            clothesSizeClothesRepository.save(newSize)
            newSize = ClothesSizeClothes()
        }

        // Process and save selected colors for the new clothes item
        for (item in clothesDTO.selectedColors) {
            val addedColor = colorsRepository.findById(item.toLong()).orElse(null)
            newColor.colors = addedColor
            newColor.clothes = newClothes
            clothesColorsRepository.save(newColor)
            newColor = ClothesColors()
        }

        // Return ResponseEntity with the newly created clothes item and HTTP status code
        return ResponseEntity(newClothes, HttpStatus.CREATED)
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