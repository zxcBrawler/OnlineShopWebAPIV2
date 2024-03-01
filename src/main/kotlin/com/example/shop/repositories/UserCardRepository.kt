package com.example.shop.repositories

import com.example.shop.models.UserCard
import org.springframework.data.repository.CrudRepository

interface UserCardRepository: CrudRepository<UserCard, Long> {

    fun findByUserId(userId : Long) : List<UserCard>

    fun findByCardId (cardId : Long) : UserCard

    fun deleteByCardId (cardId : Long) : Any
}