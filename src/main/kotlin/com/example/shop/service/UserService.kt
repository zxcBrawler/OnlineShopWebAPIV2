package com.example.shop.service

import com.example.shop.models.User
import com.example.shop.repositories.UserRepository
import org.mindrot.jbcrypt.BCrypt
import org.springframework.stereotype.Service

@Service
class UserService (private val userRepository: UserRepository) {
    fun saveUser(user : User): User {
        return this.userRepository.save(user)
    }

    fun findByEmail(email: String): User?{
        return this.userRepository.findByEmail(email)
    }
    fun setPassword(user: User) : String {
        user.passwordHash = BCrypt.hashpw(user.passwordHash, BCrypt.gensalt())
        return user.passwordHash
    }
    fun comparePassword(password: String, user: User): Boolean{
        return BCrypt.checkpw(password, user.passwordHash)
    }
}