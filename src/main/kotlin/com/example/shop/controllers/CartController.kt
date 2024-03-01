package com.example.shop.controllers

import com.example.shop.models.Cart
import com.example.shop.models.dto.CartDTO
import com.example.shop.models.dto.Message
import com.example.shop.repositories.CartRepository
import com.example.shop.repositories.ClothesColorsRepository
import com.example.shop.repositories.ClothesSizeClothesRepository
import com.example.shop.repositories.UserRepository
import jakarta.transaction.Transactional
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/cart")
class CartController (@Autowired private val cartRepository: CartRepository,
                      @Autowired private val clothesColorsRepository: ClothesColorsRepository,
                      @Autowired private val userRepository: UserRepository,
                      @Autowired private val clothesSizeClothesRepository: ClothesSizeClothesRepository
)
{
    @GetMapping("")
    fun getAllCart(): List<Cart> =
        cartRepository.findAll().toList()

    @GetMapping("/{id}")
    fun getCartByUserId(@PathVariable("id") userId: Long): List<Cart> {
        return cartRepository.findByUserId(userId).toList()
    }

    @GetMapping("/user/{userId}/clothes/{clothesId}/size/{sizeId}/color/{colorId}")
    fun checkIfItemExistsInCart(
        @PathVariable userId: Long,
        @PathVariable sizeId: Long,
        @PathVariable colorId: Long,
        @PathVariable clothesId : Long
    ): Any {

        val existingCart = cartRepository
            .findBySizeClothesSizeClothesIdAndColorClothesCartColorsColorIdAndSizeClothesClothesIdClothesAndUserId(
                sizeId, colorId, clothesId, userId)

        return existingCart?.id ?: -1
    }

    @Transactional
    @PostMapping("")
    fun createCart(cart: CartDTO): ResponseEntity<Cart> {
       val existingColorClothesCart = this.clothesColorsRepository.findById(cart.colorClothes.toLong()).orElse(null)
        val existingUser = this.userRepository.findById(cart.user.toLong()).orElse(null)
        val existingSizeClothes = this.clothesSizeClothesRepository.findById(cart.sizeClothes.toLong()).orElse(null)
        val newCart = Cart()
        newCart.user = existingUser
        newCart.quantity = cart.quantity
        newCart.sizeClothes  = existingSizeClothes
        newCart.colorClothesCart = existingColorClothesCart
        cartRepository.save(newCart)
        return ResponseEntity(newCart, HttpStatus.CREATED)
    }
    @Transactional
    @PutMapping("/{id}")
    fun updateCartById(@PathVariable("id") cartId: Long, updateType : Int, userId: Long): ResponseEntity<Any> {

        val existingCart = cartRepository.findById(cartId).orElse(null)
            ?: return ResponseEntity(HttpStatus.NOT_FOUND)

        if (updateType == 1) {
            existingCart.quantity++
        }
        else {
            existingCart.quantity--
        }
        cartRepository.save(existingCart)
        return ResponseEntity(cartRepository.findByUserId(userId), HttpStatus.OK)
    }
    @Transactional
    @DeleteMapping("/{id}/{userId}")
    fun deleteCartById(@PathVariable("id") cartId: Long, @PathVariable("userId") userId: Long): ResponseEntity<Any> {
        if (!cartRepository.existsById(cartId)) {
            return ResponseEntity(HttpStatus.NOT_FOUND)
        }
        cartRepository.deleteById(cartId)
        return ResponseEntity(cartRepository.findByUserId(userId), HttpStatus.OK)
    }
    @Transactional
    @DeleteMapping("/deleteAll/{id}")
    fun deleteAllByUserId(@PathVariable("id") userId: Long): ResponseEntity<Any> {
        cartRepository.deleteAllByUserId(userId)
        return ResponseEntity(Message("Successfully deleted"), HttpStatus.OK)
    }
}