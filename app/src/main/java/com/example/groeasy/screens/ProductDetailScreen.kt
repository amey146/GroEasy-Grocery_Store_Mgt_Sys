package com.example.groeasy.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.groeasy.viewmodel.ProductViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailScreen(
    sharedViewProducts: ProductViewModel
) {
    val product = sharedViewProducts.getProduct()

    if (product != null) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(
                text = "Product Details",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            ProductDetailItem(label = "Name", value = product.name)
            ProductDetailItem(label = "Description", value = product.desc)
            ProductDetailItem(label = "Brand", value = product.brand)
            ProductDetailItem(label = "Category", value = product.category)
            ProductDetailItem(label = "Price", value = "₹${String.format("%.2f", product.price)}")
            ProductDetailItem(label = "Quantity", value = product.unit.toString())
            ProductDetailItem(label = "Weight per unit", value = product.quantity.toString())
            ProductDetailItem(label = "Expiry", value = formatDate(product.expiry))
        }
    }
}
@Composable
fun ProductDetailItem(label: String, value: String) {
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
                    .weight(1f)
                    .align(Alignment.CenterVertically)
            )
            Spacer(modifier = Modifier.width(8.dp)) // Adds spacing between label and value
            Text(
                text = value,
                modifier = Modifier.align(Alignment.CenterVertically)
            )
        }
    }
}


@Composable
fun formatDate(expiry: Long): String {
    // Format the expiry date according to your requirements
    // For example, you can convert it to a readable date format
    val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    return dateFormat.format(Date(expiry))
}
