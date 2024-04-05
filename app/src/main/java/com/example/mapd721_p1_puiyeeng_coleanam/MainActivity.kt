package com.example.mapd721_p1_puiyeeng_coleanam

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mapd721_p1_puiyeeng_coleanam.ui.theme.MAPD721P1PuiYeeNgColeAnamTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MAPD721P1PuiYeeNgColeAnamTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainScreen()
                }
            }
        }
    }
}

data class Product(val productId: Int, val productName: String, val price: Double)

@Composable
fun MainScreen() {

    val GroceriesList : List<Product> = listOf(
        Product(
            productId = 6,
            productName = "Watermelon",
            price = 2.99,
        ),
        Product(
            productId = 2,
            productName = "Orange",
            price = 0.99,
        ),
        Product(
            productId = 3,
            productName = "Banana",
            price = 1.69,
        ),
        Product(
            productId = 4,
            productName = "Strawberry",
            price = 3.69,
        ),
        Product(
            productId = 5,
            productName = "Blueberry",
            price = 2.99,
        ),
        Product(
            productId = 6,
            productName = "Watermelon",
            price = 2.99,
        )

    )

    Column(modifier = Modifier.fillMaxWidth()) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(40.dp)
                .background(Color.LightGray),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Easy Grocery",
                fontSize = 20.sp,
                textAlign = TextAlign.Start,
                modifier = Modifier.padding(start = 16.dp)
            )
            Spacer(modifier = Modifier.weight(1f))
            IconButton(
                onClick = {
//                    showPopupBox(
//                        context,
//                        savedProductIDState.value,
//                        savedProductNameState.value,
//                        savePriceState.value
//                    )
                },
                modifier = Modifier.padding(end = 16.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.ShoppingCart,
                    contentDescription = "Shopping Cart"
                )
            }
            Button(
                onClick = {


                },
                modifier = Modifier.padding(end = 16.dp)
            ) {
                Text(text = "Clear")
            }
        }

        Spacer(modifier = Modifier.height(10.dp))
        ProductList(products = GroceriesList)
    }
}


private fun showPopupBox(context: Context, productId: Int?, productName: String?, productPrice: Double?) {
    val builder = AlertDialog.Builder(context)

    builder.setTitle("Shopping Cart")
    if(productId == 0 && productName == "" && productPrice == 0.0){
        builder.setMessage("Nothing is in the cart.")
    }
    else {
        builder.setMessage("Product ID: $productId\nProduct Name: $productName\nPrice: $$productPrice")
    }
    builder.setPositiveButton("OK") { dialog, which ->
        // Handle OK button click
        dialog.dismiss() // Dismiss the dialog
    }
    val dialog: AlertDialog = builder.create()
    dialog.show()
}


@Composable
fun ProductList(products: List<Product>) {
    LazyColumn {
        items(products) { product ->
            ProductItem(product)
        }
    }
}

@Composable
fun ProductItem(product: Product) {
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

                // Add any other product details you want to display
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    MAPD721P1PuiYeeNgColeAnamTheme {
        MainScreen()
    }
}