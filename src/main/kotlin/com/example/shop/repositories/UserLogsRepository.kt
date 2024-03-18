package com.example.shop.repositories

import com.example.shop.models.logs.UserLogs
import org.springframework.data.repository.CrudRepository

interface UserLogsRepository : CrudRepository<UserLogs, Long> {
}