package com.example.shop

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication(scanBasePackages = ["com.example.shop"])
class ShopApplication

fun main(args: Array<String>) {
	runApplication<ShopApplication>(*args)
}
