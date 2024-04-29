package com.example.groeasy.experimental

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.groeasy.Screens
import com.example.groeasy.model.DatabaseHelper
import com.example.groeasy.screens.HS
import com.example.groeasy.screens.showDatePicker
import com.example.groeasy.viewmodel.ProductViewModel
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@OptIn(DelicateCoroutinesApi::class)
@Composable
fun UpdateProduct(
    sharedViewProducts: ProductViewModel
) {
    val product = sharedViewProducts.getProduct()
    val context = LocalContext.current
    val dbhelper = DatabaseHelper.getDb(context)
    val productDao = dbhelper.productDao()

    if (product != null) {
        val state = rememberScrollState()
        LaunchedEffect(Unit) { state.animateScrollTo(100) }
        Column(
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .verticalScroll(state)
        ) {
            Text(
                text = "Product Details",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            ProductUpdateItem(label = "Name", value = product.name) { newName ->
                // Update the product name in the view model when the value changes
                product.name = newName
            }
            ProductUpdateItem(label = "Description", value = product.desc) { newDesc ->
                // Update the product description in the view model when the value changes
                product.desc = newDesc
            }

            ProductUpdateItem(label = "Brand", value = product.brand) { newBrand ->
                // Update the product brand in the view model when the value changes
                product.brand = newBrand
            }

            ProductUpdateItem(label = "Category", value = product.category) { newCategory ->
                // Update the product category in the view model when the value changes
                product.category = newCategory
            }

            ProductUpdateItem(
                label = "Price",
                value = "₹${String.format("%.2f", product.price)}"
            ) { newPrice ->
                // Update the product price in the view model when the value changes
                // Assuming newPrice is a string representation of price
                product.price = (newPrice.toDoubleOrNull() ?: 0.0).toFloat()
            }

            ProductUpdateItem(
                label = "Quantity",
                value = product.quantity.toString()
            ) { newQuantity ->
                // Update the product quantity in the view model when the value changes
                product.quantity = newQuantity.toFloatOrNull() ?: 0.toFloat()
            }

            ProductUpdateItem(label = "Unit", value = product.unit.toString()) { newUnit ->
                // Update the product unit in the view model when the value changes
                product.unit = newUnit.toIntOrNull() ?: 0
            }

            product.expiry = showDatePicker()

            Button(onClick = {
                Toast.makeText(context, "Successfully updated", Toast.LENGTH_SHORT).show()
                GlobalScope.launch {
                    productDao.updateProduct(product)
                }
                HS.getNav().navigate(Screens.HomeScreen.screen){
                    popUpTo(0)
                }
            }) {
                Text(text = "Submit")
            }
        }
    }
}

@Composable
fun ProductUpdateItem(label: String, value: String, onUpdate: (String) -> Unit) {
    var text by remember { mutableStateOf(value) }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = MaterialTheme.shapes.small,
        tonalElevation = 1.dp,
        shadowElevation = 1.dp
    ) {
        Row(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = label,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .align(Alignment.CenterVertically)
            )
            Spacer(modifier = Modifier.width(8.dp)) // Adds spacing between label and value
            OutlinedTextField(
                value = text,
                onValueChange = {
                    text = it
                    onUpdate(it) // Call the provided onUpdate lambda when the value changes
                },
                modifier = Modifier.align(Alignment.CenterVertically)
            )
        }
    }
}

