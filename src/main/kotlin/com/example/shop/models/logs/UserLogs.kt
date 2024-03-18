package com.example.shop.models.logs

import com.example.shop.models.User
import jakarta.persistence.*


@Entity
@Table(name = "userLogs")
data class UserLogs(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0,

    @ManyToOne
    var user: User? = User(),

    var timestamp: String = "",

    var description: String = "",
)