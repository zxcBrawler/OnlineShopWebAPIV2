package com.example.shop.controllers

import com.example.shop.models.Card
import com.example.shop.models.Cart
import com.example.shop.models.UserCard
import com.example.shop.models.dto.Message
import com.example.shop.models.dto.UserCardDTO
import com.example.shop.repositories.CardRepository
import com.example.shop.repositories.UserCardRepository
import com.example.shop.repositories.UserRepository
import jakarta.transaction.Transactional
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

/**
 * Controller for managing user cards.
 *
 * Users with role 'buyer' can get all their cards, add a new card for payment and
 * delete in profile settings of a mobile app
 *
 * @property userCardRepository The repository for UserCard entities.
 * @property userRepository The repository for User entities.
 * @property cardRepository The repository for Card entities.
 */
@RestController
@RequestMapping("/api/userCard")
class UserCardController (
    @Autowired private val userCardRepository: UserCardRepository,
    @Autowired private val userRepository: UserRepository,
    @Autowired private val cardRepository: CardRepository) {

    /**
     * Retrieves a list of all user cards.
     *
     * @return List<UserCard> Returns a list of all user cards.
     */
    @GetMapping("")
    fun getAllCards(): List<UserCard> =
        userCardRepository.findAll().toList()

    /**
     * Retrieves a list of user cards based on the provided user ID.
     *
     * @param userId The ID of the user for whom to retrieve the associated user cards.
     * @return ResponseEntity<List<UserCard>> Returns a list of user cards along with an HTTP status.
     */
    @GetMapping("/{id}")
    fun getCardsByUserId(@PathVariable("id") userId: Long): ResponseEntity<List<UserCard>> {
        // Fetch user cards associated with the provided user ID
        val cards = userCardRepository.findByUserId(userId)

        // Return the list of user cards along with the HTTP status indicating success
        return ResponseEntity(cards, HttpStatus.OK)
    }

    /**
     * Creates a new user card for payments and associates it with a user.
     *
     * @param userCard UserCardDTO object containing id of user that will be associated with new card,
     * user card details such as card number, CVV and expiration date.
     * @return ResponseEntity<UserCard> Returns the newly created UserCard along with an HTTP status 201.
     */
    @Transactional
    @PostMapping("")
    fun createUserCard(userCard: UserCardDTO): ResponseEntity<UserCard> {

        // Fetch the user based on the provided userId
        val user = userRepository.findById(userCard.userId).orElse(null)

        // Create a new instance of Card with details from the UserCardDTO
        val newCard = Card()
        newCard.cardNum = userCard.cardNum
        newCard.cvv = userCard.cvv
        newCard.expDate = userCard.expDate

        // Save the new card details to the repository
        cardRepository.save(newCard)

        // Create a new instance of UserCard and link it with the user and the newly created card
        val newUserCard = UserCard()
        newUserCard.user = user
        newUserCard.card = cardRepository.findById(newCard.id).orElse(null)

        // Save the new UserCard to the repository
        userCardRepository.save(newUserCard)

        // Return the newly created UserCard along with the HTTP status indicating success
        return ResponseEntity(newUserCard, HttpStatus.CREATED)
    }

    @Transactional
    @DeleteMapping("/{id}")
    fun deleteUserCartById(@PathVariable("id") cardId: Long): ResponseEntity<Any> {
        userCardRepository.deleteByCardId(cardId)

        return ResponseEntity(Message("Successfully deleted"),HttpStatus.OK)
    }
}