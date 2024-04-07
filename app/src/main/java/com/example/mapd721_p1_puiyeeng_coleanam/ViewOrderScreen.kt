package com.example.mapd721_p1_puiyeeng_coleanam

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.mapd721_p1_puiyeeng_coleanam.model.Order
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ViewOrderScreen(navController: NavController) {
    var orderId by remember { mutableStateOf(TextFieldValue()) }
    var passCode by remember { mutableStateOf(TextFieldValue()) }
    var order by remember { mutableStateOf<Order?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                    text = "Search Order", textAlign = TextAlign.Center,
                ) },
                navigationIcon = {
                    IconButton(
                        onClick = { navController.popBackStack() },
                    ) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        content = { padding ->
            Box(
                modifier = Modifier.fillMaxSize().padding(padding)
            ) {
            Column(modifier = Modifier.padding(16.dp)) {
                // Search box
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextField(
                        value = orderId,
                        onValueChange = { orderId = it },
                        label = { Text("Order ID") },
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    TextField(
                        value = passCode,
                        onValueChange = { passCode = it },
                        label = { Text("Passcode") },
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Button(onClick = {
                        searchOrder(orderId.text, passCode.text)
                        { fetchedOrder ->
                            // Handle the fetched order here
                            if (fetchedOrder != null) {
                                // Order found
                                order = fetchedOrder
                            } else {
                                // Order not found or incorrect passCode
                                // Handle the case accordingly
                            }
                        }
                    }) {
                        Text("Search")
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Order details
                order?.let { fetchedOrder ->
                    Text(
                        text = "Order Summary",
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                    Column {
                        Text("Order ID: ${fetchedOrder.orderId}")
                        Text("Customer Name: ${fetchedOrder.customerName}")
                        Text("Delivery Option: ${fetchedOrder.deliveryOption}")
                        if (fetchedOrder.deliveryOption == "Pickup") {
                            Text("Pickup Address: ${fetchedOrder.address}")
                        }
                        else{
                            Text("Shipping Address: ${fetchedOrder.address}")
                        }
                        Text("Order Date: ${fetchedOrder.orderDate}")
                        if (fetchedOrder.deliveryOption == "Pickup") {
                            Text("Pickup Date: ${fetchedOrder.pickupDate}")
                        }
                        else{
                            Text("Delivery Date: ${fetchedOrder.pickupDate}")
                        }
                        Text("Total Price: ${fetchedOrder.totalPrice}")

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = "Product List",
                            modifier = Modifier.align(Alignment.CenterHorizontally),
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                        // Product list
                        LazyColumn {
                            items(fetchedOrder.productList) { product ->
                                ProductCard(product = product)
                                Spacer(modifier = Modifier.height(8.dp))
                            }
                        }
                    }
                }
            }
        }
        }
    )
}

private val ordersRef: DatabaseReference = FirebaseDatabase.getInstance().getReference("orders")

private fun searchOrder(orderId: String, passCode: String, callback: (Order?) -> Unit) {
    // Query the order with the specified orderId and passCode
    val query: Query = ordersRef
        .orderByChild("orderId")
        .equalTo(orderId)
        .limitToFirst(1)

    query.addListenerForSingleValueEvent(object : ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            if (dataSnapshot.exists()) {
                val orderSnapshot: DataSnapshot = dataSnapshot.child(orderId)
                val fetchedOrder: Order? = orderSnapshot.getValue(Order::class.java)

                // Check if the fetched order has the correct passCode
                if (fetchedOrder?.passCode == passCode) {
                    // Order found and passCode is correct
                    callback(fetchedOrder)
                } else {
                    // Incorrect passCode
                    callback(null)
                }
            } else {
                // Order not found
                callback(null)
            }
        }

        override fun onCancelled(databaseError: DatabaseError) {
            // Error occurred while querying the database
            callback(null)
        }
    })
}