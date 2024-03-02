package com.example.shop.service

import com.example.shop.models.User
import com.example.shop.repositories.UserRepository
import org.mindrot.jbcrypt.BCrypt
import org.springframework.stereotype.Service
import java.util.Optional

@Service
class UserService (private val userRepository: UserRepository) {
    fun saveUser(user : User): User {
        return this.userRepository.save(user)
    }

    fun findByUsername(username: String): Optional<User>?{
        return this.userRepository.findByUsername(username)
    }
    fun setPassword(password: String) : String {
        return BCrypt.hashpw(password, BCrypt.gensalt())
    }
    fun comparePassword(password: String, user: User): Boolean{
        return BCrypt.checkpw(password, user.password)
    }
}