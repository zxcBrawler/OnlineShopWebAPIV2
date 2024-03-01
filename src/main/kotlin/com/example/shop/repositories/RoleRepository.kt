package com.example.shop.repositories

import com.example.shop.models.Role
import org.springframework.data.repository.CrudRepository

interface RoleRepository : CrudRepository<Role, Long> {}