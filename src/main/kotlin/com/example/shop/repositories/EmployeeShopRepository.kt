package com.example.shop.repositories

import com.example.shop.models.EmployeeShop
import org.springframework.data.repository.CrudRepository

interface EmployeeShopRepository : CrudRepository<EmployeeShop, Long> {

    fun getAllByShopAddressesShopAddressId(shopAddressId : Long) : List<EmployeeShop>

    fun getEmployeeShopByEmployeeId(employeeId : Long) : EmployeeShop
}