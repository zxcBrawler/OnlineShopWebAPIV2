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

@RestController
@RequestMapping("/api/userCard")
class UserCardController (
    @Autowired private val userCardRepository: UserCardRepository,
    @Autowired private val userRepository: UserRepository,
    @Autowired private val cardRepository: CardRepository) {

    @GetMapping("")
    fun getAllCards(): List<UserCard> =
        userCardRepository.findAll().toList()

    @GetMapping("/{id}")
    fun getCardsByUserId(@PathVariable("id") userId: Long): ResponseEntity<List<UserCard>> {
        val card = userCardRepository.findByUserId(userId)

        return ResponseEntity(card, HttpStatus.OK)
    }

    @Transactional
    @PostMapping("")
    fun createUserCard(userCard: UserCardDTO): ResponseEntity<UserCard> {

        val user = userRepository.findById(userCard.userId).orElse(null)

        val newCard = Card()
        newCard.cardNum = userCard.cardNum
        newCard.cvv = userCard.cvv
        newCard.expDate = userCard.expDate

        cardRepository.save(newCard)

        val newUserCard = UserCard()
        newUserCard.user = user
        newUserCard.card = cardRepository.findById(newCard.id).orElse(null)

        userCardRepository.save(newUserCard)

        return ResponseEntity(newUserCard, HttpStatus.CREATED)
    }

    @Transactional
    @DeleteMapping("/{id}")
    fun deleteUserCartById(@PathVariable("id") cardId: Long): ResponseEntity<Any> {
        userCardRepository.deleteByCardId(cardId)

        return ResponseEntity(Message("Successfully deleted"),HttpStatus.OK)
    }
}