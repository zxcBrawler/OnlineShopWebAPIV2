package com.example.shop.repositories

import com.example.shop.models.ShopGarnish
import org.springframework.data.repository.CrudRepository

interface ShopGarnishRepository: CrudRepository<ShopGarnish, Long> {

   fun getAllByColorClothesGarnishIdAndSizeClothesGarnishId (sizeId : Long, colorId : Long) : List<ShopGarnish>

   fun findBySizeClothesGarnishIdAndColorClothesGarnishIdAndShopAddressesGarnishShopAddressId (sizeId: Long, colorId: Long, shopAddressId: Long) : ShopGarnish


   fun getAllByShopAddressesGarnishShopAddressId(shopAddressId : Long) : List<ShopGarnish>
}