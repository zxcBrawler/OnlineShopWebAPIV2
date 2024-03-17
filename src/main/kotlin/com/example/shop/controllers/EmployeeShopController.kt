package com.example.shop.controllers

import com.example.shop.models.EmployeeShop
import com.example.shop.repositories.EmployeeShopRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@CrossOrigin(maxAge = 3600)
@RestController
@RequestMapping("/api/employeeShop")
class EmployeeShopController (@Autowired private val employeeShopRepository: EmployeeShopRepository) {


    @GetMapping("/{shopAddressId}")
    fun getAllEmployeesByShopId(@PathVariable shopAddressId : Long) : ResponseEntity<List<EmployeeShop>>{
        return ResponseEntity(employeeShopRepository.getAllByShopAddressesShopAddressId(shopAddressId), HttpStatus.OK)
    }

    @GetMapping("/employee/{employeeId}")
    fun getShopAddressByEmployeeId(@PathVariable employeeId : Long) : ResponseEntity<EmployeeShop>{

        val foundEmployeeShop = employeeShopRepository.getEmployeeShopByEmployeeId(employeeId);
        return ResponseEntity(foundEmployeeShop, HttpStatus.OK)
    }

}