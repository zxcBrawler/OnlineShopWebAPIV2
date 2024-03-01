package com.example.shop.models

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*


@Entity
@Table(name = "user_card")
data class UserCard(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    //DONE
    @ManyToOne(cascade = [CascadeType.ALL])
    var user: User = User(),

    //DONE
    @ManyToOne(cascade = [CascadeType.ALL])
    var card: Card = Card(),

    @JsonIgnore
    @OneToMany(mappedBy = "userCard", cascade = [CascadeType.ALL])
    val cardOrder : List<Order> = arrayListOf()
)
