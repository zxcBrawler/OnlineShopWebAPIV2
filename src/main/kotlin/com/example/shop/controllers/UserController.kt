package com.example.shop.controllers

import com.example.shop.models.User
import com.example.shop.models.dto.RegisterDTO
import com.example.shop.models.dto.UpdateUserDTO
import com.example.shop.repositories.CategoryClothesRepository
import com.example.shop.repositories.RoleRepository
import com.example.shop.repositories.UserRepository
import com.example.shop.service.UserService
import org.mindrot.jbcrypt.BCrypt
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@CrossOrigin(maxAge = 3600)
@RestController
@RequestMapping(value = ["/api/users"])
class UserController (
    @Autowired private val userRepository: UserRepository,
    @Autowired private val categoryClothesRepository: CategoryClothesRepository,
    @Autowired private val roleRepository: RoleRepository,
    @Autowired private val userService: UserService) {
    @GetMapping("")
    fun getAllUsers(): List<User> =
        userRepository.findAll().toList()


    //ADMIN ONLY
    /**
     * Creates a new user based on the provided [user] data.
     *
     * @param user The [RegisterDTO] containing the information for the new user.
     * @return ResponseEntity<User> The response entity containing the newly created user information.
     */
    @PostMapping("")
    fun createUser(user: RegisterDTO): ResponseEntity<User> {
        // Create a new User instance


        // Retrieve the existing gender from the repository based on the user's gender ID
        val existingGender = categoryClothesRepository.findById(user.gender.toLong()).orElse(null)

        // Retrieve the existing role from the repository based on the user's role ID
        val existingRole = roleRepository.findById(user.role.toLong()).orElse(null)


        val newUser = User(username = user.username, passwordHash = userService.setPassword(user.passwordHash))
        // Set the properties of the new user
        newUser.role = existingRole
        newUser.gender = existingGender
        newUser.email = user.email
        newUser.employeeNumber = user.employeeNumber
        newUser.phoneNumber = user.phoneNumber
        // Set the profile photo of the new user
        newUser.profilePhoto = user.profilePhoto

        // Return the response entity with the newly created user information and HttpStatus.CREATED
        return ResponseEntity.ok(this.userService.saveUser(newUser))
    }


    @GetMapping("/{id}")
    fun getUserById(@PathVariable("id") userId: Long): ResponseEntity<User> {
        val user = userRepository.findById(userId).orElse(null)
        return if (user != null) ResponseEntity(user, HttpStatus.OK)
        else ResponseEntity(HttpStatus.NOT_FOUND)
    }

    @PutMapping("/{id}")
            /**
             * Updates a user by their ID with the provided [user] data.
             *
             * @param userId The ID of the user to be updated.
             * @param user The [RegisterDTO] containing the updated user information.
             * @return ResponseEntity<User> The response entity containing the updated user information.
             */
    fun updateUserById(@PathVariable("id") userId: Long, user: UpdateUserDTO): ResponseEntity<User> {

        // Retrieve the existing user from the repository based on the provided [userId]
        val existingUser = userRepository.findById(userId).orElse(null)
            ?: return ResponseEntity(HttpStatus.NOT_FOUND)

        // Retrieve the existing gender from the repository based on the user's gender ID
        val existingGender = categoryClothesRepository.findById(user.gender.toLong()).orElse(null)

        // Retrieve the existing role from the repository based on the user's role ID
        val existingRole = roleRepository.findById(user.role.toLong()).orElse(null)

        // Check if the provided password is different from the existing user's password


        // Create an updated user object with the new information
        val updatedUser = existingUser.copy(
            username = user.username,

            phoneNumber = user.phoneNumber,
            profilePhoto = user.profilePhoto,
            email = user.email,
            gender = existingGender,
            role = existingRole,
            employeeNumber = user.employeeNumber
        )

        // Save the updated user to the repository
        userRepository.save(updatedUser)

        // Return the response entity with the updated user information and HttpStatus.OK
        return ResponseEntity(updatedUser, HttpStatus.OK)
    }

    @DeleteMapping("/{id}")
    fun deleteUserById(@PathVariable("id") userId: Long): ResponseEntity<User> {
        if (!userRepository.existsById(userId)) {
            return ResponseEntity(HttpStatus.NOT_FOUND)
        }
        userRepository.deleteById(userId)
        return ResponseEntity(HttpStatus.NO_CONTENT)
    }

}
