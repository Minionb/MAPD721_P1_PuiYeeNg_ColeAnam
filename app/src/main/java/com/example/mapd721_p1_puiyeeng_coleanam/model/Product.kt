package com.example.mapd721_p1_puiyeeng_coleanam.model
data class Product(
    val productId: Int = 0,
    val productName: String = "",
    val price: Double = 0.0,
    var quantity: Int = 0,
    val imagePath: String = ""
) {
    // Default constructor
    constructor() : this(0, "", 0.0, 0, "")
}