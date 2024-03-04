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

/**
 * Controller class handling operations related to the user's shopping cart.
 *
 * @param cartRepository The repository for accessing and manipulating Cart entities.
 * @param clothesColorsRepository The repository for accessing and manipulating ClothesColors entities.
 * @param userRepository The repository for accessing and manipulating User entities.
 * @param clothesSizeClothesRepository The repository for accessing and manipulating ClothesSizeClothes entities.
 */
@RestController
@RequestMapping("/api/cart")
class CartController (
    @Autowired private val cartRepository: CartRepository,
    @Autowired private val clothesColorsRepository: ClothesColorsRepository,
    @Autowired private val userRepository: UserRepository,
    @Autowired private val clothesSizeClothesRepository: ClothesSizeClothesRepository
) {

    /**
     * Retrieves a list of all items in the shopping cart.
     *
     * @return List<Cart> Returns a list containing all Cart entities.
     */
    @GetMapping("")
    fun getAllCart(): List<Cart> =
        cartRepository.findAll().toList()

    /**
     * Retrieves the shopping cart items for a specific user identified by the provided user ID.
     *
     * @param userId The unique identifier of the user for whom to retrieve the shopping cart.
     * @return List<Cart> Returns a list containing Cart entities associated with the user.
     */
    @GetMapping("/{id}")
    fun getCartByUserId(@PathVariable("id") userId: Long): List<Cart> {
        return cartRepository.findByUserId(userId).toList()
    }

    /**
     * Checks if a specific piece of clothes with specific size and color exists in the shopping cart for certain user.
     *
     * @param userId The unique identifier of the user.
     * @param sizeId The unique identifier of the size.
     * @param colorId The unique identifier of the color.
     * @param clothesId The unique identifier of the clothes.
     * @return Any Returns the ID of the existing cart item, or -1 if not found.
     */
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

    /**
     * Creates a new cart item for the user's shopping cart.
     *
     * @param cart CartDTO object containing details of the new cart item.
     * @return ResponseEntity<Cart> Returns the newly created Cart item along with an HTTP status 201.
     */
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

    /**
     * Updates the quantity of a cart item identified by the provided cart ID for a specific user.
     *
     * @param cartId The unique identifier of the cart item to be updated.
     * @param updateType An integer representing the type of update (1 for increment, -1 for decrement).
     * @param userId The unique identifier of the user.
     * @return ResponseEntity<Any> Returns the updated shopping cart along with an HTTP status 200.
     */
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

    /**
     * Deletes a specific cart item identified by the provided cart ID for a specific user.
     *
     * @param cartId The unique identifier of the cart item to be deleted.
     * @param userId The unique identifier of the user.
     * @return ResponseEntity<Any> Returns the updated shopping cart along with an HTTP status 200.
     */
    @Transactional
    @DeleteMapping("/{id}/{userId}")
    fun deleteCartById(@PathVariable("id") cartId: Long, @PathVariable("userId") userId: Long): ResponseEntity<Any> {
        if (!cartRepository.existsById(cartId)) {
            return ResponseEntity(HttpStatus.NOT_FOUND)
        }
        cartRepository.deleteById(cartId)
        return ResponseEntity(cartRepository.findByUserId(userId), HttpStatus.OK)
    }

    /**
     * Deletes all cart items for a specific user identified by the provided user ID.
     * Used when user forms an order with items from cart
     *
     * @param userId The unique identifier of the user.
     * @return ResponseEntity<Any> Returns a message indicating successful deletion along with an HTTP status 200.
     */
    @Transactional
    @DeleteMapping("/deleteAll/{id}")
    fun deleteAllByUserId(@PathVariable("id") userId: Long): ResponseEntity<Any> {
        cartRepository.deleteAllByUserId(userId)
        return ResponseEntity(Message("Successfully deleted"), HttpStatus.OK)
    }
}
