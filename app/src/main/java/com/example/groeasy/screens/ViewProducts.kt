package com.example.groeasy.screens

import android.app.AlertDialog
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import com.example.groeasy.R
import com.example.groeasy.Screens
import com.example.groeasy.model.DatabaseHelper
import com.example.groeasy.model.Product
import com.example.groeasy.model.ProductDao
import com.example.groeasy.ui.theme.quicksandRegularfont
import com.example.groeasy.viewmodel.ProductViewModel
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.text.DateFormat
import java.util.Date
import java.util.Locale


class ViewProducts {

    private lateinit var navControllerVar: NavController
    fun setNav(navController: NavController) {
        navControllerVar = navController
    }

    fun getNav(): NavController {
        return navControllerVar
    }
}

val VP = ViewProducts()

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ViewProducts(
    category: String?,
    navController: NavController,
    sharedViewProducts: ProductViewModel
) {
    val context = LocalContext.current
    val dbhelper = DatabaseHelper.getDb(context)
    val productDao = dbhelper.productDao()
    var products by remember { mutableStateOf<List<Product>>(emptyList()) }
    val lifecycleOwner = LocalLifecycleOwner.current
    VP.setNav(navController)
    DisposableEffect(category) {
        val liveData = productDao.getProductsByCategory(category)
        val observer = Observer<List<Product>> { newProducts ->
            products = newProducts
        }
        liveData.observe(lifecycleOwner, observer)

        onDispose {
            liveData.removeObserver(observer)
        }
    }

    if (products.isEmpty()) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            TopAppBar(title = { /*TODO*/ }, navigationIcon = {
                IconButton(onClick = { VP.getNav().navigate(Screens.HomeScreen.screen) }) {
                    Icon(
                        imageVector = Icons.Rounded.ArrowBack,
                        contentDescription = "Back"
                    )
                }
            })
            Image(
                painter = painterResource(id = R.drawable.emptycart),
                contentDescription = null,
                modifier = Modifier.size(300.dp)
            )
            Text(text = "NO STOCK FOUND", fontSize = 32.sp, fontFamily = quicksandRegularfont)
        }
    } else {
        ListOfProducts(products, productDao, sharedViewProducts, context)
    }
}


@OptIn(DelicateCoroutinesApi::class, ExperimentalMaterial3Api::class)
@Composable
fun ListOfProducts(
    productsByCategory: List<Product>,
    productDao: ProductDao,
    sharedViewProducts: ProductViewModel,
    context: Context
) {

    val itemsList = productsByCategory.size



    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TopAppBar(title = { /*TODO*/ }, navigationIcon = {
                IconButton(onClick = { VP.getNav().navigate(Screens.HomeScreen.screen) }) {
                    Icon(
                        imageVector = Icons.Rounded.ArrowBack,
                        contentDescription = "Back"
                    )
                }
            })
            Text(
                text = productsByCategory[0].category,
                fontSize = 40.sp,
                modifier = Modifier.padding(25.dp)
            )
            LazyColumn(
                modifier = Modifier.padding(25.dp),
            ) {
                items(itemsList) { index ->
                    fun compareDates(expiryDateMillis: Long): Int {
                        val todayMillis = System.currentTimeMillis()
                        val today = Date(todayMillis)
                        val expiryDate = Date(expiryDateMillis)

                        // Compare the dates
                        return today.compareTo(expiryDate)
                    }

                    val expiryDateMillis = productsByCategory[index].expiry
                    val result = compareDates(expiryDateMillis)
                    var thatColor = MaterialTheme.colorScheme.background
                    if (result > 0) {
                        thatColor = Color(0xFFFF4533)
                    }
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                val product = productsByCategory[index]
                                sharedViewProducts.setProduct(product)
                                VP
                                    .getNav()
                                    .navigate(Screens.ProductDetailScreen.screen)
                            },
                        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = thatColor,
                        ),

                        ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.Star,
                                contentDescription = null,
                                modifier = Modifier
                                    .size(80.dp)
                                    .padding(end = 16.dp)
                            )

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = productsByCategory[index].name,
                                    style = TextStyle(
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Bold
                                    ),
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "Quantity: ${productsByCategory[index].unit}",
                                    style = TextStyle(fontSize = 16.sp)
                                )
                                Text(
                                    text = "Unit: ${productsByCategory[index].quantity}",
                                    style = TextStyle(fontSize = 16.sp)
                                )
                                val date = Date(productsByCategory[index].expiry)

                                val formatter = DateFormat.getDateInstance(
                                    DateFormat.MEDIUM,
                                    Locale.getDefault()
                                )

                                val formattedDate = formatter.format(date)

                                Text(
                                    text = "Expiry: $formattedDate",
                                    style = TextStyle(fontSize = 16.sp)
                                )
                            }

                            Column {
//                                IconButton(
//                                    onClick = {
//                                        val product = productsByCategory[index]
//                                        sharedViewProducts.setProduct(product)
//                                        VP.getNav().navigate(Screens.ProductUpdateScreen.screen)
//                                    },
//                                    modifier = Modifier.padding(end = 8.dp)
//                                ) {
//                                    Icon(
//                                        imageVector = Icons.Rounded.Edit,
//                                        contentDescription = "Edit",
//                                        tint = Color.Gray
//                                    )
//                                }
                                IconButton(

                                    onClick = {
                                        val builder1: AlertDialog.Builder =
                                            AlertDialog.Builder(context)
                                        builder1.setMessage("Are you sure you want to delete?")
                                        builder1.setCancelable(true)

                                        builder1.setPositiveButton(
                                            "Yes"
                                        ) { dialog, _ ->
                                            dialog.cancel()
                                            Toast.makeText(
                                                context,
                                                "Product Deleted",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                            GlobalScope.launch {
                                                productDao.deleteProduct(productsByCategory[index])
                                            }
                                        }
                                        builder1.setNegativeButton(
                                            "No"
                                        ) { dialog, _ -> dialog.cancel() }
                                        val alert11: AlertDialog = builder1.create()
                                        alert11.show()

                                    },
                                    modifier = Modifier.padding(end = 8.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Rounded.Delete,
                                        contentDescription = "Delete",
                                        tint = Color.Gray
                                    )
                                }
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                }
            }
        }
    }
}
