package com.example.shop.models.dto

class ClothesDTO {
    var nameClothesRu = "";
    var nameClothesEn = "";
    var barcode = "";
    var priceClothes = "";
    var selectedTypeClothes = 0;
    var selectedSizes : MutableList<Int> = arrayListOf();
    var selectedColors : MutableList<Int> = arrayListOf();
    var uploadedPhotos : MutableList<String> = arrayListOf();
}