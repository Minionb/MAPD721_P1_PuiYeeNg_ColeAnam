package com.example.mapd721_p1_puiyeeng_coleanam.model

data class Order(
    val orderId: String,
    val passCode: String,
    val productList: List<Product>,
    val totalPrice: Double,
    val customerName: String,
    val deliveryOption: String,
    val address: String,
    val orderDate: String,
    val pickupDate: String,
) {
    constructor() : this(
        orderId = "",
        passCode = "",
        productList = emptyList(),
        totalPrice = 0.0,
        customerName = "",
        deliveryOption = "",
        address = "",
        orderDate = "",
        pickupDate = "",
    )
}
