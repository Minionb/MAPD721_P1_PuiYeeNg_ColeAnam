package com.example.mapd721_p1_puiyeeng_coleanam.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.mapd721_p1_puiyeeng_coleanam.Product
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.first
import java.math.BigDecimal
import java.math.RoundingMode

class StoreProductInfo(private val context: Context) {

    // Companion object to create a single instance of DataStore for username, email and id
    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("products_data_store")

        // Keys to uniquely identify username, email and id in DataStore
        val PRODUCTS_KEY = stringPreferencesKey("products")
    }

    suspend fun saveProducts( productToAdd: Product) {
        val existingProducts = readProducts().toMutableList()

        // Check if the product with the given ID already exists in the list
        val existingProductIndex = existingProducts.indexOfFirst { it.productId == productToAdd.productId }

        if (existingProductIndex != -1) {
            // If the product already exists, increment its quantity by one, update its price
            val existingProduct = existingProducts[existingProductIndex]
            val updatedQuantity =  existingProduct.quantity + 1
            val updatedPrice = BigDecimal(productToAdd.price * (updatedQuantity)).setScale(2, RoundingMode.HALF_UP).toDouble()
            val updatedProduct = existingProduct.copy(quantity = existingProduct.quantity + 1, price = updatedPrice)
            existingProducts[existingProductIndex] = updatedProduct
        } else {
            // If the product doesn't exist, add it to the list with quantity 1
            existingProducts.add(Product(productToAdd.productId, productToAdd.productName, productToAdd.price,1, productToAdd.imagePath))
        }

        context.dataStore.edit { preferences ->
            preferences[PRODUCTS_KEY] = serializeProducts(existingProducts)
        }
    }

    suspend fun readProducts(): List<Product> {
        val preferences = context.dataStore.data.first()
        val productsString = preferences[PRODUCTS_KEY] ?: return emptyList()
        return deserializeProducts(productsString)
    }

    suspend fun clearProducts() {
        context.dataStore.edit { preferences ->
            preferences.clear()
        }
    }
}

private val gson = Gson()

fun serializeProducts(products: List<Product>): String {
    return gson.toJson(products)
}

fun deserializeProducts(productsString: String): List<Product> {
    val type = object : TypeToken<List<Product>>() {}.type
    return gson.fromJson(productsString, type)
}
