package com.example.mapd721_p1_puiyeeng_coleanam.firebase.model

import com.example.mapd721_p1_puiyeeng_coleanam.Product

data class Order(
    val orderId: String,
    val passCode: String,
    val productList: List<Product>,
    val totalPrice: Double,
    val customerName: String,
    val deliveryOption: String,
    val address: String
)
