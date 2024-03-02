package com.example.shop.models

import com.example.shop.models.logs.UserLogs
import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

@Entity
@Table(name = "user")
data class User (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
     var id: Long = 0,
    @Column(unique = true)
    private var username: String = "",
    @Column(length = 60)
    private var passwordHash: String = "",
    @Column(unique = true)
    var email: String = "",
    @Column(unique = true)
    var phoneNumber: String = "",

    var profilePhoto: String = "",

    var employeeNumber: String? = "",


    @Enumerated(EnumType.STRING)
    @ManyToOne
    var role : Role = Role(),

    @ManyToOne
    var gender : CategoryClothes = CategoryClothes(),

    @JsonIgnore
    @OneToMany(mappedBy = "user")
    val cart : List<Cart> = arrayListOf(),

    @JsonIgnore
    @OneToMany(mappedBy = "user")
    val userAddress : List<UserAddress> = arrayListOf(),

    @JsonIgnore
    @OneToMany(mappedBy = "user")
    val userCard : List<UserCard> = arrayListOf(),

    @JsonIgnore
    @OneToMany(mappedBy = "user")
    val userOrder : List<UserOrder> = arrayListOf(),

    @JsonIgnore
    @OneToMany(mappedBy = "user")
    val userLog : List<UserLogs> = arrayListOf(),
    @JsonIgnore
    @OneToMany(mappedBy = "employee")
    val employeeShop : List<EmployeeShop> = arrayListOf(),
) : UserDetails {
    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        return mutableListOf(SimpleGrantedAuthority(role.roleName))
    }

    override fun getPassword(): String {
        return passwordHash
    }

    override fun getUsername(): String {
        return username
    }

    override fun isAccountNonExpired(): Boolean {
        return true
    }

    override fun isAccountNonLocked(): Boolean {
        return true
    }

    override fun isCredentialsNonExpired(): Boolean {
        return true
    }

    override fun isEnabled(): Boolean {
        return true
    }

}

