package com.example.shop.controllers.auth

import com.example.shop.config.JwtService
import com.example.shop.models.User
import com.example.shop.models.dto.LoginDTO

import com.example.shop.models.dto.LoginResponse
import com.example.shop.models.dto.Message
import com.example.shop.models.dto.RegisterDTO
import com.example.shop.models.logs.UserLogs
import com.example.shop.repositories.CategoryClothesRepository
import com.example.shop.repositories.RoleRepository
import com.example.shop.repositories.UserLogsRepository
import com.example.shop.service.UserService
import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.bind.annotation.*
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

/**
 * Controller for handling user authentication-related operations.
 *
 * @property userService The service for managing user-related operations.
 * @property categoryClothesRepository The repository for CategoryClothes entities.
 * @property roleRepository The repository for Role entities.
 * @property jwtService The service for handling JWT token-related operations.
 * @property passwordEncoder The password encoder for encoding and decoding passwords.
 */
@CrossOrigin(maxAge = 3600)
@RestController
@RequestMapping(value = ["/api/auth"])
class AuthController (
    private val userService: UserService,
    @Autowired private val categoryClothesRepository: CategoryClothesRepository,
    @Autowired private val roleRepository: RoleRepository,
    @Autowired private val userLogsRepository: UserLogsRepository,
    private val jwtService: JwtService,
    private val passwordEncoder: PasswordEncoder,

) {
    private val logger = LoggerFactory.getLogger(this::class.java)
    /**
     * Handles user registration.
     *
     * @param body The registration details in the form of a RegisterDTO.
     * @return ResponseEntity<User> Returns the newly registered user along with an HTTP status.
     */
    @PostMapping("/register")
    fun register(
       @RequestBody body: RegisterDTO) : ResponseEntity<User> {
        // Fetch the existing gender from the repository based on the provided gender ID
        val existingGender = categoryClothesRepository.findById(body.gender.toLong()).orElse(null)

        // Create a new user with the provided registration details
        val user = User(
            username = body.username,
            passwordHash = passwordEncoder.encode(body.passwordHash)
        )
        // registration only can be done for users with role id 4 which is role named 'buyer'
        val buyerRole = roleRepository.findById(4).orElse(null)
        val userLogs = UserLogs()
        // Set additional details for the user
        user.email = body.email
        user.phoneNumber = body.phoneNumber
        user.profilePhoto = body.profilePhoto
        user.gender = existingGender
        user.role = buyerRole
        user.employeeNumber = body.employeeNumber
        userService.saveUser(user)

        logger.warn("New buyer with username ${user.username} has been registered")
        userLogs.user = user
        userLogs.timestamp = LocalDate.now().toString() + " " +
         LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"))
        userLogs.description = "New user with username ${user.username} has been registered"
        userLogsRepository.save(userLogs)

        // Save the user to the repository and return the response
        return ResponseEntity.ok(user)
    }

    /**
     * Handles user login.
     *
     * @param body The login details in the form of a LoginDTO.
     * @param response The HTTP response object for setting cookies.
     * @return ResponseEntity<Any> Returns the login response containing user details and JWT token along with an HTTP status.
     */
    @PostMapping("/login")
    fun login( body: LoginDTO, response: HttpServletResponse) : ResponseEntity<LoginResponse>{
        // Create a new login response object
        val loginResponse = LoginResponse()

        val userLogs = UserLogs()

        // Find the user by the provided username
        val userOptional = this.userService.findByUsername(body.username)

        if (userOptional == null) {


                logger.error("User with username ${body.username} has not been found")
                userLogs.user = null
                userLogs.timestamp = LocalDate.now().toString() + " " +
                        LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"))
                userLogs.description = "User with username ${body.username} has not been found"
                userLogsRepository.save(userLogs)

                loginResponse.message = "user not found"
                return ResponseEntity(loginResponse, HttpStatus.NOT_FOUND)

        }

        val user = userOptional?.get()

// Check if the provided password matches the user's password
        if (!this.userService.comparePassword(body.passwordHash, user!!)) {
            logger.error("Invalid password for user ${body.username}")
            userLogs.user = null
            userLogs.timestamp = LocalDate.now().toString() + " " +
                    LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"))
            userLogs.description = "Invalid password for user ${body.username}"
            userLogsRepository.save(userLogs)


            loginResponse.message = "invalid password"


            return ResponseEntity(loginResponse, HttpStatus.BAD_REQUEST)
        }

        // Generate a JWT token for the user
        val jwt = user.let { jwtService.generateToken(it) }

        // Create an HTTP-only cookie with the JWT token and add it to the response
        val cookie = Cookie("jwt", jwt)
        cookie.isHttpOnly = true
        loginResponse.user = user
        loginResponse.accessToken = jwt
        response.addCookie(cookie)

        logger.info("New login to account with username ${body.username}")
        userLogs.user = loginResponse.user
        userLogs.timestamp = LocalDate.now().toString() + " " +
                LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"))
        userLogs.description = "New login to account with username ${body.username}"
        userLogsRepository.save(userLogs)

        // Return the login response along with the HTTP status
        return ResponseEntity.ok(loginResponse)
    }

    /**
     * Handles the change of user password.
     *
     * @param username The username of the user for whom to change the password.
     * @param newPassword The new password to set.
     * @return ResponseEntity<Any> Returns a response indicating the status of the password change.
     */
    @PostMapping("/changePassword")
    fun changePassword(@RequestParam username: String, @RequestParam newPassword: String): ResponseEntity<Any> {
        // Find the user by the provided username
        val userOptional = userService.findByUsername(username)?.orElse(null)
        val userLogs = UserLogs()
        // Check if the user exists
        if (userOptional == null) {

                logger.error("User with username $username has not been found")
                userLogs.user = null
                userLogs.timestamp = LocalDate.now().toString() + " " +
                        LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"))
                userLogs.description = "User with username $username has not been found"
                userLogsRepository.save(userLogs)
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Message("User not found"))
        }

        try {
            // Update the user's password and save it to the repository
            val user = userOptional
            val updatedUser = user.copy(passwordHash = userService.setPassword(newPassword))
            logger.info("New password set for user with username ${updatedUser.username}")
            userLogs.user = updatedUser
            userLogs.timestamp = LocalDate.now().toString() + " " +
                    LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"))
            userLogs.description = "New password set for user with username ${updatedUser.username}"
            userLogsRepository.save(userLogs)
            userService.saveUser(updatedUser)

            // Return a response indicating successful password update
            return ResponseEntity.ok().build()
        } catch (e: Exception) {
            // Return a response indicating failure to update the password
            logger.error("Failed to update password for user with username $username")
            userLogs.user = null
            userLogs.timestamp = LocalDate.now().toString() + " " +
                    LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"))
            userLogs.description = "Failed to update password for user with username $username"
            userLogsRepository.save(userLogs)
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Message("Failed to update password"))
        }
    }
}