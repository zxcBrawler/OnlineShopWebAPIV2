package com.example.shop.repositories

import com.example.shop.models.User
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository: CrudRepository<User, Long> {
    fun findByEmail(email: String): User?


}