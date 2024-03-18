package com.example.shop.repositories

import com.example.shop.models.ShopGarnish
import org.springframework.data.repository.CrudRepository

interface ShopGarnishRepository: CrudRepository<ShopGarnish, Long> {

   fun getShopGarnishBySizeClothesGarnishSizeClothesIdAndColorClothesGarnishId (sizeId : Long, colorId : Long) : List<ShopGarnish>


   fun getAllByShopAddressesGarnishShopAddressId(shopAddressId : Long) : List<ShopGarnish>
}