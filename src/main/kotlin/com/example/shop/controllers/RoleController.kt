package com.example.shop.controllers

import com.example.shop.models.Role
import com.example.shop.repositories.RoleRepository

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@CrossOrigin(maxAge = 3600)
@RestController
@RequestMapping("/api/role")
class RoleController(@Autowired private val roleRepository: RoleRepository) {
    @GetMapping("")
    fun getAllRoles(): List<Role> =
        roleRepository.findAll().toList()
    @GetMapping("/{id}")
    fun getRoleById(@PathVariable("id") roleId: Long): ResponseEntity<Role> {
        val role = roleRepository.findById(roleId).orElse(null)
        return if (role != null) ResponseEntity(role, HttpStatus.OK)
        else ResponseEntity(HttpStatus.NOT_FOUND)
    }

    @PostMapping("")
    fun createRole(@RequestBody role: Role): ResponseEntity<Role> {
        val createdRole = roleRepository.save(role)
        return ResponseEntity(createdRole, HttpStatus.CREATED)
    }
    @PutMapping("/{id}")
    fun updateRoleById(@PathVariable("id") roleId: Long, @RequestBody role: Role): ResponseEntity<Role> {

        val existingRole = roleRepository.findById(roleId).orElse(null)
            ?: return ResponseEntity(HttpStatus.NOT_FOUND)

        val updatedRole = existingRole.copy(
            roleName = role.roleName,

        )
        roleRepository.save(updatedRole)
        return ResponseEntity(updatedRole, HttpStatus.OK)
    }

    @DeleteMapping("/{id}")
    fun deleteUserById(@PathVariable("id") roleId: Long): ResponseEntity<Role> {
        if (!roleRepository.existsById(roleId)) {
            return ResponseEntity(HttpStatus.NOT_FOUND)
        }
        roleRepository.deleteById(roleId)
        return ResponseEntity(HttpStatus.NO_CONTENT)
    }
}