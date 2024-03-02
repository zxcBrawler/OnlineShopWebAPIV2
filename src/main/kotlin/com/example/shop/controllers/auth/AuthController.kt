package com.example.shop.controllers.auth

import com.example.shop.config.JwtService
import com.example.shop.models.Role
import com.example.shop.models.User
import com.example.shop.models.dto.LoginDTO

import com.example.shop.models.dto.LoginResponse
import com.example.shop.models.dto.Message
import com.example.shop.models.dto.RegisterDTO
import com.example.shop.repositories.CategoryClothesRepository
import com.example.shop.repositories.RoleRepository
import com.example.shop.service.UserService
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletResponse
import org.mindrot.jbcrypt.BCrypt
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatusCode
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping(value = ["/api/auth"])
class AuthController (private val userService: UserService,
                      @Autowired private val categoryClothesRepository: CategoryClothesRepository,
    @Autowired private val roleRepository: RoleRepository,
    private val jwtService: JwtService,
    private val authenticationManager : AuthenticationManager,
    private val passwordEncoder: PasswordEncoder
    ) {

    @PostMapping("/register")
    fun register(@RequestBody body: RegisterDTO) : ResponseEntity<User> {
        val existingGender = categoryClothesRepository.findById(body.gender.toLong()).orElse(null)
        val user = User(username = body.username, passwordHash = passwordEncoder.encode(body.passwordHash))
        val buyerRole = roleRepository.findById(4).orElse(null)
        user.email = body.email
        user.phoneNumber = body.phoneNumber
        user.profilePhoto = body.profilePhoto
        user.gender = existingGender
        user.role = buyerRole
        user.employeeNumber = body.employeeNumber
        return ResponseEntity.ok(this.userService.saveUser(user))
    }

    @PostMapping("/login")
    fun login(@RequestBody body: LoginDTO, response: HttpServletResponse) : ResponseEntity<Any>{
        val loginResponse = LoginResponse()
        val user = this.userService.findByUsername(body.username)
            ?: return ResponseEntity.badRequest().body(Message("user not found"))

        if(!this.userService.comparePassword(body.passwordHash, user.get())){
            return ResponseEntity.badRequest().body(Message("Invalid password"))
        }

       val jwt = jwtService.generateToken(user.get())
        val cookie = Cookie("jwt",jwt)
        cookie.isHttpOnly = true
        loginResponse.user = user.get()
        loginResponse.accessToken = jwt
        response.addCookie(cookie)

        return ResponseEntity.ok(loginResponse)
    }

    @PostMapping("/changePassword")
    fun changePassword(@RequestParam username: String, @RequestParam newPassword: String): ResponseEntity<Any> {
        val userOptional = userService.findByUsername(username)

        if (userOptional != null) {
            if (userOptional.isEmpty) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Message("User not found"))
            }
        }

        try {
            val user = userOptional?.get()
            val updatedUser = user?.copy(passwordHash = userService.setPassword(newPassword))
            if (updatedUser != null) {
                userService.saveUser(updatedUser)
            }

            return ResponseEntity.ok().build()
        } catch (e: Exception) {

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Message("Failed to update password"))
        }
    }

}