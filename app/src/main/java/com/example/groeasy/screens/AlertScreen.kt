package com.example.groeasy.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Observer
import com.example.groeasy.R
import com.example.groeasy.model.DatabaseHelper
import com.example.groeasy.model.Product
import com.example.groeasy.ui.theme.quicksandRegularfont
import java.text.DateFormat
import java.time.LocalDate
import java.time.ZoneOffset
import java.util.Date
import java.util.Locale

@Composable
fun NotifScreen() {
    val context = LocalContext.current
    val dbhelper = DatabaseHelper.getDb(context)
    val productDao = dbhelper.productDao()
    var products: List<Product> by remember { mutableStateOf(emptyList()) }
    val lifecycleOwner = LocalLifecycleOwner.current

    val limit = sliderExample()

    val today = LocalDate.now()
    val upcoming = today.plusDays(limit.toLong())

    val todayInMillis = today.atStartOfDay().toInstant(ZoneOffset.UTC).toEpochMilli()
    val upcomingInMillis = upcoming.atStartOfDay().toInstant(ZoneOffset.UTC).toEpochMilli()

    DisposableEffect(limit) {
        val liveData = productDao.getDatesWithinNextDays(todayInMillis, upcomingInMillis)
        val observer = Observer<List<Product>> { newProducts ->
            products = newProducts
        }
        liveData.observe(lifecycleOwner, observer)

        onDispose {
            liveData.removeObserver(observer)
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(0.dp, 110.dp, 0.dp, 0.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            if (products.isNotEmpty()) {
                Text(text = "Alerts", fontSize = 40.sp, modifier = Modifier.padding(25.dp))
                Spacer(modifier = Modifier.height(20.dp))
                ThreeLineListItemWithOverlineAndSupporting(products)
            } else {
                Image(
                    painter = painterResource(id = R.drawable.good),
                    contentDescription = null,
                    modifier = Modifier.size(300.dp)
                )
                Text(text = "NO ALERTS", fontSize = 32.sp, fontFamily = quicksandRegularfont)
            }
        }
    }
}

@Composable
fun sliderExample(): Int {
    var sliderValue by remember { mutableFloatStateOf(10f) }

    Card(modifier = Modifier.padding(16.dp)) {
        Column(modifier = Modifier.padding(10.dp)) {
            Text(
                text = "Expiry Days limit: ${sliderValue.toInt()}",
                modifier = Modifier.padding(top = 16.dp),
                fontSize = 18.sp
            )
            Slider(
                value = sliderValue,
                onValueChange = { newValue ->
                    sliderValue = newValue
                },
                valueRange = 1f..30f,
                steps = 5,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
    return sliderValue.toInt()
}

@Composable
fun ThreeLineListItemWithOverlineAndSupporting(product: List<Product>) {

    LazyColumn {
        items(product.size) { idx ->
            ListItem(
                headlineContent = { Text(product[idx].name) },
                overlineContent = { Text(product[idx].brand) },
                supportingContent = {
                    val date = Date(product[idx].expiry)

                    val formatter = DateFormat.getDateInstance(
                        DateFormat.MEDIUM,
                        Locale.getDefault()
                    )

                    val formattedDate = formatter.format(date)

                    Text(formattedDate)
                },
                leadingContent = {
                    Icon(
                        Icons.Rounded.Star,
                        contentDescription = null,
                    )
                },
                trailingContent = { Text("Quantity: "+product[idx].unit.toString()) }
            )
            Divider()
        }
    }
}

