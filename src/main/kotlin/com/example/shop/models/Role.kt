package com.example.shop.models

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*

@Entity
@Table(name = "role")
data class Role(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0,
    @Column(unique = true)
    var roleName : String = "",

    @JsonIgnore
    @OneToMany(mappedBy = "role")
    var users : List<User> = arrayListOf()
)
