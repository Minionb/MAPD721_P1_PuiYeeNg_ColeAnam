package com.example.mapd721_p1_puiyeeng_coleanam.model

data class Order(
    val orderId: String,
    val passCode: String,
    val productList: List<Product>,
    val totalPrice: Double,
    var customerName: String,
    var deliveryOption: String,
    var address: String,
    var orderDate: String,
    var pickupDate: String,
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
