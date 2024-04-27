package com.example.groeasy.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeightIn
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material.icons.twotone.KeyboardArrowRight
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.DockedSearchBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.isTraversalGroup
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.traversalIndex
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import com.example.groeasy.MyImage
import com.example.groeasy.R
import com.example.groeasy.Screens
import com.example.groeasy.model.DatabaseHelper
import com.example.groeasy.model.Product
import com.example.groeasy.model.ProductDao
import com.example.groeasy.ui.theme.quicksandRegularfont
import com.example.groeasy.viewmodel.ProductViewModel

class HomeScreen {

    private lateinit var navControllerVar: NavController
    fun setNav(navController: NavController) {
        navControllerVar = navController
    }

    fun getNav(): NavController {
        return navControllerVar
    }
}

val HS = HomeScreen()

@Composable
fun HomeScreen(navController: NavController, sharedViewProducts: ProductViewModel) {
    val state = rememberScrollState()
    LaunchedEffect(Unit) { state.animateScrollTo(100) }
    Column(
        modifier = Modifier
            .verticalScroll(state)
    ) {
        HS.setNav(navController)
        val myImages = listOf(
            MyImage(R.drawable.vege, "Groceries"),
            MyImage(R.drawable.bakery, "Bakery"),
            MyImage(R.drawable.snacks, "Snacks"),
            MyImage(R.drawable.dairy, "Dairy"),
            MyImage(R.drawable.house, "Households"),
            MyImage(R.drawable.frozen, "Frozen-Foods"),
            MyImage(R.drawable.others, "Others")
        )
        Column {
            val context1 = LocalContext.current
            val databaseHelper = DatabaseHelper.getDb(context1)
            val productDao = databaseHelper.productDao()

            GreetCard(productDao, sharedViewProducts)
            Spacer(modifier = Modifier.height(30.dp))
            Text(
                text = "View Stocks",
                modifier = Modifier.padding(10.dp),
                fontSize = 32.sp,
            )
            Spacer(modifier = Modifier.height(10.dp))
            Row {
                Spacer(modifier = Modifier.width(10.dp))
                RectCategoryRow(
                    images = myImages
                )
            }
            Spacer(modifier = Modifier.height(10.dp))

            var products by remember { mutableStateOf<List<Product>>(emptyList()) }
            val lifecycleOwner = LocalLifecycleOwner.current

            DisposableEffect(true) {
                val liveData = productDao.getProductOrderId()
                val observer = Observer<List<Product>> { newProducts ->
                    products = newProducts
                }
                liveData.observe(lifecycleOwner, observer)

                onDispose {
                    liveData.removeObserver(observer)
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
            if (products.isNotEmpty()) {
                Text(
                    text = "Recently Added:",
                    modifier = Modifier.padding(10.dp),
                    fontSize = 32.sp,
                )
                OneLineListItem(products)
            } else {
                Text(
                    text = "No Products?",
                    modifier = Modifier.padding(10.dp, 0.dp),
                    fontSize = 32.sp,
                )
                Button(
                    onClick = {
                        HS.getNav().navigate(Screens.AddProduct.screen)
                    }, modifier = Modifier
                        .padding(12.dp)
                ) {
                    Text(
                        text = "Add Here"
                    )
                }
            }
        }
    }
}

@Composable
fun GreetCard(productDao: ProductDao, sharedViewProducts: ProductViewModel) {

    Box(
        modifier = Modifier
            .background(
                Color(33, 203, 145), RoundedCornerShape(
                    topStart = 0.dp,
                    topEnd = 0.dp,
                    bottomStart = 35.dp,
                    bottomEnd = 35.dp
                )
            )

    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp, 0.dp)
        ) {
            Spacer(modifier = Modifier.height(30.dp))
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                val isDarkTheme = isSystemInDarkTheme() // Determine if the theme is dark

                val iconColor = if (isDarkTheme) Color(34, 47, 55, 255) else Color.White
                Text(
                    color = Color.White,
                    text = "Hello User 👋",
                    fontWeight = FontWeight.Bold,
                    fontSize = 35.sp,
                )
                Icon(
                    imageVector = Icons.Rounded.AccountCircle,
                    contentDescription = null,
                    Modifier.size(55.dp),
                    tint = iconColor
                )
            }
            Spacer(modifier = Modifier.height(20.dp))
            DockedSearchBarSample(productDao, sharedViewProducts)
            Row(
                modifier = Modifier
                    .padding(16.dp, 0.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "What are you looking for today?",
                        style = TextStyle(
                            fontFamily = FontFamily(Font(R.font.quicksand)),
                            fontWeight = FontWeight.Bold,
                            fontSize = 22.sp,
                            color = Color.Black,
                        ),
                    )
                }
                Image(
                    painter = painterResource(id = R.drawable.decor),
                    contentDescription = null,
                    modifier = Modifier
                        .align(Alignment.Bottom)
                        .padding(top = 10.dp)
                )
            }

        }
    }
}


@Composable
fun RectCategoryRow(images: List<MyImage>) {

    val colorList = listOf(
        Color(0xFFfffc5d),
        Color(0xFFfc4589),
        Color(0xFFe51010),
        Color(0xFF00b2ff),
        Color(0xFF420e87),
        Color(0xFF4adeaf),
        Color(0xFFff6600),
    )
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(15.dp)
    ) {
        items(images.size) { index ->
            val imageResId = images[index].resId
            val caption = images[index].title
            Card(
                modifier = Modifier
                    .size(160.dp)
                    .clickable {
                        HS
                            .getNav()
                            .navigate(Screens.ViewProducts.screen + "/$caption")
                    },
                colors = CardDefaults.cardColors(
                    containerColor = colorList[(index % colorList.size)]
                ),
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(10.dp)
                ) {
                    Text(
                        text = caption,
                        style = TextStyle(
                            color = colorList[index % colorList.size].let { color ->
                                if (color == Color(0xFFfffc5d) || color == Color(0xFF4adeaf)) {
                                    Color.Black
                                } else {
                                    Color.White
                                }
                            },
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = quicksandRegularfont,
                        ),
                    )

                    Spacer(modifier = Modifier.height(10.dp)) // Adding space between text and image
                    Image(
                        painter = painterResource(id = imageResId),
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }
    }
}

@Composable
fun OneLineListItem(product: List<Product>) {

    Column {
        product.forEach { idx ->
            ListItem(
                headlineContent = { Text(product[product.indexOf(idx)].name) },
                leadingContent = {
                    Icon(
                        Icons.Filled.CheckCircle,
                        contentDescription = "Localized description",
                    )
                }
            )
            Divider()
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DockedSearchBarSample(productDao: ProductDao, sharedViewProducts: ProductViewModel) {
    var text by rememberSaveable { mutableStateOf("") }
    var active by rememberSaveable { mutableStateOf(false) }
    var searchResults by remember { mutableStateOf<List<Product>>(productDao.getProductSearchEmpty()) }
    Box(Modifier.semantics { isTraversalGroup = true }) {
        DockedSearchBar(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 8.dp)
                .semantics { traversalIndex = -1f },
            query = text,
            onQueryChange = {
                text = it
                searchResults = productDao.searchProducts(text.trim())
            },
            onSearch = { active = false },
            active = active,
            onActiveChange = { active = it },
            placeholder = { Text("Search for Products...") },
            leadingIcon = { Icon(Icons.Rounded.Search, contentDescription = null) },
            trailingIcon = { Icon(Icons.Rounded.MoreVert, contentDescription = null) },
        ) {
            if (searchResults.isEmpty()) {
                Text(
                    "No Product",
                    modifier = Modifier.fillMaxWidth(),
                    fontSize = 30.sp,
                    textAlign = TextAlign.Center
                )
            } else if (text.trim() == "") {
                searchResults.forEach { product ->
                    ListItem(
                        headlineContent = { Text(product.name) },
                        modifier = Modifier
                            .clickable {
                                text = product.name
                                active = false
                                sharedViewProducts.setProduct(product)
                                HS
                                    .getNav()
                                    .navigate(Screens.ProductDetailScreen.screen)
                            }
                            .fillMaxWidth()
                            .requiredHeightIn()
                            .padding(horizontal = 16.dp, vertical = 4.dp),
                        leadingContent = {
                            Icon(
                                Icons.Rounded.Star,
                                contentDescription = null
                            )
                        }
                    )
                }
            } else {
                searchResults.forEach { product ->
                    ListItem(
                        headlineContent = { Text(product.name) },
                        modifier = Modifier
                            .clickable {
                                text = product.name
                                active = false
                                sharedViewProducts.setProduct(product)
                                HS
                                    .getNav()
                                    .navigate(Screens.ProductDetailScreen.screen)
                            }
                            .fillMaxWidth()
                            .requiredHeightIn()
                            .padding(horizontal = 16.dp, vertical = 4.dp),
                        leadingContent = {
                            Icon(
                                Icons.TwoTone.KeyboardArrowRight,
                                contentDescription = null
                            )
                        }
                    )
                }
            }
        }
    }
}