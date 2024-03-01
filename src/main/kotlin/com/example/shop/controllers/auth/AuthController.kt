package com.example.shop.controllers.auth

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
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping(value = ["/api/auth"])
class AuthController (private val userService: UserService,
                      @Autowired private val categoryClothesRepository: CategoryClothesRepository,
    @Autowired private val roleRepository: RoleRepository
    ) {

    @PostMapping("/register")
    fun register(body: RegisterDTO) : ResponseEntity<User> {
        val existingGender = categoryClothesRepository.findById(body.gender.toLong()).orElse(null)
        val user = User()
        val buyerRole = roleRepository.findById(4).orElse(null)
        user.username = body.username
        user.email = body.email
        user.phoneNumber = body.phoneNumber
        user.profilePhoto = body.profilePhoto
        user.gender = existingGender
        user.passwordHash = body.passwordHash
        user.role = buyerRole
        user.passwordHash = userService.setPassword(user)
        user.employeeNumber = body.employeeNumber
        return ResponseEntity.ok(this.userService.saveUser(user))
    }

    @PostMapping("/login")
    fun login(body: LoginDTO, response: HttpServletResponse) : ResponseEntity<Any>{
        val loginResponse = LoginResponse()
        val user = this.userService.findByEmail(body.email)
            ?: return ResponseEntity.badRequest().body(Message("user not found"))

        if(!this.userService.comparePassword(body.passwordHash, user)){
            return ResponseEntity.badRequest().body(Message("Invalid password"))
        }

//       val issuer = user.id.toString()
//        val jwt = Jwts.builder()
//            .setIssuer(issuer)
//            .setExpiration(Date(System.currentTimeMillis() + 60 * 24 * 1000)) //1 day
//            .signWith(SignatureAlgorithm.HS512, "secret").compact()
//        val cookie = Cookie("jwt", jwt)
//        cookie.isHttpOnly = true

        loginResponse.user = user
       // loginResponse.accessToken = jwt

       // response.addCookie(cookie)
        return ResponseEntity.ok(loginResponse)
    }

    @PostMapping("/changePassword")
    fun changePassword(email : String, newPassword : String) : ResponseEntity<Any>{
        val user = this.userService.findByEmail(email)
            ?: return ResponseEntity.badRequest().body(Message("user not found"))

        user.passwordHash = newPassword

        val newUser = user.copy(
            passwordHash = userService.setPassword(user)
        )
        userService.saveUser(newUser)

        return ResponseEntity(HttpStatus.OK)
    }
}