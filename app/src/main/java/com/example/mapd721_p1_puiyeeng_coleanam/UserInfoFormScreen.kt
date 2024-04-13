package com.example.mapd721_p1_puiyeeng_coleanam

import android.os.Build
import android.widget.ListView
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.mapd721_p1_puiyeeng_coleanam.model.Order
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserInfoFormScreen(navController: NavController, order: Order) {
    var nameFirst by remember { mutableStateOf(TextFieldValue()) }
    var nameLast by remember { mutableStateOf(TextFieldValue()) }
    var userAddress by remember { mutableStateOf(TextFieldValue()) }
    var userPostal by remember { mutableStateOf(TextFieldValue()) }
    var userCity by remember { mutableStateOf(TextFieldValue()) }
    var userCountry by remember { mutableStateOf(TextFieldValue()) }
    var userProvince by remember { mutableStateOf(TextFieldValue()) }
    var storeLocation by remember { mutableStateOf(TextFieldValue()) }
    val options = arrayOf("Pick-Up", "Delivery")
    var selectedItem by remember { mutableStateOf("Not Selected") }
    val textFieldVisibility = remember { mutableStateListOf(false, false) }
    var showDialog by remember { mutableStateOf(false) }
    var expanded by remember { mutableStateOf(false) }
    var emptyFields by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val storeOptions = listOf("Toronto Downtown: 297 College St, Toronto, ON M5T 1S2", "North York: 1800 Sheppard Ave E, North York, ON M2J 5A7", "Markham: 8339 Kennedy Road, Building C, G/F, Markham, ON L3R 5T5")
    var selectedStoreOption by remember { mutableStateOf("") }

    val database = FirebaseDatabase.getInstance()
    val ordersRef = database.getReference("orders")

    println(order)

    @Composable
    fun ExposedDropdownMenu() {
        Box(
            modifier = Modifier
                .fillMaxWidth()
            //.padding(32.dp)
        ) {
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = {
                    expanded = !expanded
                }
            ) {
                TextField(
                    value = selectedItem,
                    onValueChange = {},
                    readOnly = true,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                    modifier = Modifier.menuAnchor()
                )

                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    options.forEachIndexed { index, item ->
                        DropdownMenuItem(
                            text = { Text(text = item) },
                            onClick = {
                                selectedItem = item
                                expanded = false
                                textFieldVisibility[index] = true
                                if (index == 0) {
                                    textFieldVisibility[1] = false
                                }
                                else {
                                    textFieldVisibility[0] = false
                                }
                            }
                        )
                    }
                }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Customer Info", textAlign = TextAlign.Center) },
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
            Box (
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {
                LazyColumn(contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp),
                ) {
                    item {
                        Text(text = "First Name:")
                        TextField(
                            value = nameFirst,
                            onValueChange = { nameFirst = it },
                            //modifier = Modifier.weight(1f),
                            placeholder = { Text(text = "John") }
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(text = "Last Name:")
                        TextField(
                            value = nameLast,
                            onValueChange = { nameLast = it },
                            placeholder = { Text(text = "Smith") }
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(text = "Select Option:")
                        ExposedDropdownMenu()
                        Spacer(modifier = Modifier.height(16.dp))
                        textFieldVisibility.forEachIndexed { index, visible ->
                            if (visible && index == 0) {
//                                Text(text = "Pick one of the store for pick-up")
//                                TextField(
//                                    value = storeLocation,
//                                    onValueChange = { storeLocation = it }
//                                )

                                Column {
                                    Text(text = "Pick one of the store for pick-up")

                                    storeOptions.forEach { option ->
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Checkbox(
                                                checked = selectedStoreOption == option,
                                                onCheckedChange = { selectedStoreOption = if (it) option else "" }
                                            )
                                            Text(text = option)
                                        }
                                    }
                                }
                                Spacer(modifier = Modifier.height(25.dp))
                            }
                            else if (visible && index == 1) {
                                Text(text = "Enter your address:")
                                TextField(
                                    value = userAddress,
                                    onValueChange = { userAddress = it },
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                                Text(text = "Enter your postal code:")
                                TextField(
                                    value = userPostal,
                                    onValueChange = { userPostal = it }
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                                Text(text = "Enter your city:")
                                TextField(
                                    value = userCity,
                                    onValueChange = { userCity = it }
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                                Text(text = "Enter your province:")
                                TextField(
                                    value = userProvince,
                                    onValueChange = { userProvince = it }
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                                Text(text = "Enter your country:")
                                TextField(
                                    value = userCountry,
                                    onValueChange = { userCountry = it }
                                )
                                Spacer(modifier = Modifier.height(25.dp))
                            }

                        }

                        if (emptyFields) {
                            Text(text = "One or more fields are empty", color = Color.Red)
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Button(onClick = {
                                if (nameFirst.text != "" && nameLast.text != "") {
                                    if (selectedItem == "Pick-Up" && selectedStoreOption != "") {
                                        scope.launch {
                                            emptyFields = false
                                            showDialog = true
                                        }
                                    }
                                    else if (selectedItem == "Delivery" && userAddress.text != "" && userCity.text != "" && userCountry.text != "" && userProvince.text != "" && userPostal.text != "") {
                                        scope.launch {
                                            emptyFields = false
                                            showDialog = true
                                        }
                                    }
                                    else {
                                        emptyFields = true
                                    }
                                }
                                else {
                                    emptyFields = true
                                }

                            }) {
                                Text(text = "Order")
                            }
                        }

                        if (showDialog) {
                            order.customerName = "${nameLast.text} ${nameFirst.text}"
                            order.deliveryOption = selectedItem
                            if (selectedItem == "Pick-Up") {
                                order.address = selectedStoreOption
                            }
                            else {
                                order.address = "${userAddress.text} ${userCity.text} , ${userProvince.text}, ${userCountry.text}, ${userPostal.text}"
                            }
                            order.orderDate = getCurrentDate()
                            order.pickupDate = getTomorrowDate()
                            println(order)
                            ConfirmDialog(
                                onDismiss = { showDialog = false },
                                navController = navController,
                                order = order,
                                ordersRef = ordersRef
                            )
                        }
                    }

                }
            }
        }
    )
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun getCurrentDate(): String {
    val currentDate = LocalDate.now()
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    return currentDate.format(formatter)
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun getTomorrowDate(): String {
    val currentDate = LocalDate.now().plusDays(1)
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    return currentDate.format(formatter)
}

@Composable
fun ConfirmDialog(
    onDismiss: () -> Unit,
    navController: NavController,
    order: Order,
    ordersRef: DatabaseReference,
) {
    var lastDialog by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = {
            Text(text = "Confirm Order", style = TextStyle(fontSize = 24.sp, fontWeight = FontWeight.Bold))
        },
        confirmButton = {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = "Is the information entered correct?")
                Spacer(modifier = Modifier.height(16.dp))
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Button(onClick = { onDismiss() }) {
                        Text(text = "Close")
                    }
                    Button(onClick = {
                        lastDialog = true

                        ordersRef.child(order.orderId).setValue(order)
                            .addOnSuccessListener {
                                // Order successfully added to the Realtime Database
                                println("Order added to the Realtime Database")

                            }
                            .addOnFailureListener { e ->
                                // Error adding order to the Realtime Database
                                println("Error adding order to the Realtime Database: $e")
                            }
                    }) {
                        Text(text = "Confirm")
                    }
                }
            }
        })

    if (lastDialog) {
        AlertDialog(
            onDismissRequest = { onDismiss() },
            title = {
                Text(
                    text = "Order Info",
                    style = TextStyle(fontSize = 24.sp, fontWeight = FontWeight.Bold)
                )
            },
            confirmButton = {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = "Your ${order.deliveryOption} date is: ${order.pickupDate}")
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(text = "Save your order id and order passcode for future reference.")
                    Spacer(modifier = Modifier.height(20.dp))
                    Text(text = order.orderId, style = TextStyle(fontWeight = FontWeight.Bold))
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(text = order.passCode, style = TextStyle(fontWeight = FontWeight.Bold))
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Button(onClick = {
                            onDismiss()
                            navController.navigate("home")
                        }) {
                            Text(text = "Close")
                        }
                    }

                }
            })
    }
}