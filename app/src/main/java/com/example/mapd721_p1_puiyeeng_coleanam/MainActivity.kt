package com.example.mapd721_p1_puiyeeng_coleanam

import android.app.Activity
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingClient.ProductType
import com.android.billingclient.api.BillingClientStateListener
import com.android.billingclient.api.BillingFlowParams
import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.PurchasesUpdatedListener
import com.android.billingclient.api.QueryProductDetailsParams
import com.example.mapd721_p1_puiyeeng_coleanam.ui.theme.MAPD721P1PuiYeeNgColeAnamTheme
import kotlinx.coroutines.launch
import com.example.mapd721_p1_puiyeeng_coleanam.datastore.StoreProductInfo
import com.example.mapd721_p1_puiyeeng_coleanam.model.Order
import com.example.mapd721_p1_puiyeeng_coleanam.model.Product
import com.google.common.collect.ImmutableList
import com.google.gson.Gson
import java.util.UUID

private var billingClient : BillingClient? = null

class MainActivity : ComponentActivity() {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MAPD721P1PuiYeeNgColeAnamTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {

                    EasyGroceriesApp()
                    initBilling()
                }
            }
        }
    }

    private val purchaseUpdatedListener =
        PurchasesUpdatedListener { billingResult, purchases ->

        }

    fun initBilling() {

        println("InitBilling")

        billingClient = BillingClient.newBuilder(this)
            .setListener(purchaseUpdatedListener)
            .enablePendingPurchases()
            .build()

        if (billingClient == null) {
            println("BillingClient is still null")
        } else {
            println("BillingClient is not null")
        }


    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun EasyGroceriesApp() {
    val navController = rememberNavController()

    NavHost(navController, startDestination = "home") {
        composable("home") {
            MainScreen(navController)
        }
        composable("viewOrder") {
            ViewOrderScreen(navController)
        }
        composable("userInfoForm/{order}",
            arguments = listOf(navArgument("order") { type = NavType.StringType })
        ) {navBackStackEntry ->
            navBackStackEntry.arguments?.getString("order")?.let {json ->
                val order = Gson().fromJson(json, Order::class.java)
                UserInfoFormScreen(navController, order)
            }
        }

//        composable("userInfoForm") {
//            UserInfoFormScreen(navController)
//        }
    }
}


@Composable
fun MainScreen(navController: NavController) {

    // context
    val context = LocalContext.current
    // scope
    val scope = rememberCoroutineScope()
    // datastore Product
    val dataStore = StoreProductInfo(context)

    val GroceriesList : List<Product> = listOf(
        Product(
            productId = 1,
            productName = "Watermelon",
            price = 2.99,
            imagePath = "watermelon",
            quantity = 0
        ),
        Product(
            productId = 2,
            productName = "Orange",
            price = 0.99,
            imagePath = "orange",
            quantity = 0
        ),
        Product(
            productId = 3,
            productName = "Banana",
            price = 1.69,
            imagePath = "banana",
            quantity = 0
        ),
        Product(
            productId = 4,
            productName = "Strawberry",
            price = 3.69,
            imagePath = "strawberry",
            quantity = 0
        ),
        Product(
            productId = 5,
            productName = "Blueberry",
            price = 2.99,
            imagePath = "blueberry",
            quantity = 0
        ),
        Product(
            productId = 6,
            productName = "Apple",
            price = 2.99,
            imagePath = "apple",
            quantity = 0
        )
    )

    val addedProducts = remember { mutableStateListOf<Product>() }

    var retrievedProducts by remember { mutableStateOf(
        emptyList<Product>()
    ) }

    // State to track if dialog is shown
    var showDialog by remember { mutableStateOf(false) }

    var totalPrice by remember { mutableStateOf(0.0) }

    // LaunchedEffect to perform data loading
    LaunchedEffect(Unit) {
        retrievedProducts = dataStore.readProducts()
        addedProducts.clear()
        addedProducts.addAll(retrievedProducts)
    }

    Column(modifier = Modifier.fillMaxWidth()) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(40.dp)
                .background(Color.LightGray),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Home button
            IconButton(
                onClick = { /* TODO: Handle home button click */ },
                modifier = Modifier.padding(start = 8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Home,
                    contentDescription = "Home"
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            // Check Order button
            IconButton(
                onClick = { navController.navigate("viewOrder") },
                modifier = Modifier.padding(end = 8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Check Order"
                )
            }

            Text(
                text = "Easy Grocery",
                fontSize = 20.sp,
                textAlign = TextAlign.Start,
                modifier = Modifier.padding(start = 8.dp),
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.weight(1f))
            IconButton(
                onClick = {
                    scope.launch {
                        retrievedProducts = dataStore.readProducts()
                        showDialog = true
                        totalPrice = dataStore.readProductsTotalPrice()
                    }
                },
                modifier = Modifier.padding(end = 8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.ShoppingCart,
                    contentDescription = "Shopping Cart"
                )
            }
            Button(
                onClick = {
                    scope.launch {
                        dataStore.clearProducts()
                        addedProducts.clear()
                        totalPrice = 0.0
                    }
                },
                modifier = Modifier.padding(end = 8.dp)
            ) {
                Text(text = "Clear")
            }
        }

        Spacer(modifier = Modifier.height(10.dp))
        if (showDialog) {
            ProductListDialog(
                products = retrievedProducts,
                onDismiss = { showDialog = false },
                totalPrice = totalPrice,
                //ordersRef= ordersRef,
                navController = navController
            )
        }
        ProductList(products = GroceriesList,
            addToCart = {
                    product ->
                scope.launch {
                    dataStore.saveProducts(product)
                }
            })
    }
}


@Composable
fun ProductList(products: List<Product>,  addToCart: (Product) -> Unit) {
    LazyColumn {
        items(products) { product ->
            ProductItem(product, addToCart = { addToCart(product)})
        }
    }
}

@Composable
fun ProductItem(product: Product, addToCart: () -> Unit) {
    Card(
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(8.dp)
        ) {
//            Image(
//                painter = rememberCoilPainter(product.imageUrl),
//                contentDescription = product.productName,
//                modifier = Modifier.size(64.dp)
//            )
            Column(
                modifier = Modifier.padding(start = 8.dp)
            ) {
                Text(
                    text = product.productName,
                    style = TextStyle(fontWeight = FontWeight.Bold)
                )
                Text(text = product.price.toString())
                Button(onClick = addToCart, Modifier
                    .align(Alignment.End)) {
                    Text("Add to Cart")
                }


            }
        }
    }
}

@Composable
fun ProductCard(product: Product) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondary,
        ),
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(text = "Product ID : ${product.productId}")
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "Product Name : ${product.productName}")
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "Price: $${product.price}")
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "Quantity: ${product.quantity}")
        }
    }
}





@Composable
fun ProductListDialog(
    products: List<Product>,
    onDismiss: () -> Unit,
    totalPrice: Double,
//    ordersRef: DatabaseReference,
    navController: NavController
) {
    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = { Text(
            text = "Shopping Cart",
            style = TextStyle(fontSize = 24.sp, fontWeight = FontWeight.Bold)
        ) },
        confirmButton = {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Button(onClick = { onDismiss() }) {
                    Text(text = "Close")
                }
                Button(onClick = {
                    val orderId = UUID.randomUUID().toString()
                    val passCode = generateRandomPassCode(16)
                    val order = Order(
                        orderId = orderId,
                        passCode = passCode,
                        productList = products,
                        totalPrice = totalPrice,
                        customerName = "",
                        deliveryOption = "",
                        address = "",
                        orderDate = "",
                        pickupDate = "",

                        )
                    println("Clicked")

                    billingClient!!.startConnection(object : BillingClientStateListener {
                        override fun onBillingSetupFinished(billingResult: BillingResult) {
                            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                                println("Response Code OK")
                                val queryProductDetailsParams =
                                    QueryProductDetailsParams.newBuilder()
                                        .setProductList(
                                            ImmutableList.of(
                                                QueryProductDetailsParams.Product.newBuilder()
                                                    .setProductId(order.orderId)
                                                    .setProductType(ProductType.INAPP)
                                                    .build()
                                            )
                                        )
                                        .build()

                                billingClient!!.queryProductDetailsAsync(queryProductDetailsParams) {
                                        _,
                                        productDetailsList ->
                                    val activity : Activity = MainActivity()
                                    println("Query Product Details Async")
                                    for (productDetails in productDetailsList) {
                                        val productDetailsParamsList = listOf(
                                            BillingFlowParams.ProductDetailsParams.newBuilder()
                                                .setProductDetails(productDetails)
                                                .build()
                                        )
                                        val billingFlowParams = BillingFlowParams.newBuilder()
                                            .setProductDetailsParamsList(productDetailsParamsList)
                                            .build()
                                        billingClient!!.launchBillingFlow(activity, billingFlowParams)
                                    }
                                }
                            } else {
                                println("Connection to Google Play Not OK")
                            }
                        }

                        override fun onBillingServiceDisconnected() {

                        }

                    })



//                    ordersRef.child(order.orderId).setValue(order)
//                        .addOnSuccessListener {
//                            // Order successfully added to the Realtime Database
//                            println("Order added to the Realtime Database")
//
//                        }
//                        .addOnFailureListener { e ->
//                            // Error adding order to the Realtime Database
//                            println("Error adding order to the Realtime Database: $e")
//                        }
                    println("After Clicked $order")
                    onDismiss()
                    navController.navigate("userInfoForm/${Gson().toJson(order)}")
                    //navController.navigate("userInfoForm")
                }) {
                    Text(text = "Checkout")
                }
            }
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier.weight(1f)
                ) {
                    LazyColumn {
                        items(products) { product ->
                            ProductCard(product = product)
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    }
                }
                Text(
                    text = "Total: $totalPrice",
                    modifier = Modifier.padding(top = 8.dp),
                    style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold)
                )
            }

        }
    )
}


fun generateRandomPassCode(length: Int): String {
    val charset = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"
    return (1..length)
        .map { charset.random() }
        .joinToString("")
}




//@Preview(showBackground = true)
//@Composable
//fun MainScreenPreview() {
//    MAPD721P1PuiYeeNgColeAnamTheme {
//        EasyGroceriesApp()
//    }
//}