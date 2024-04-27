package com.example.groeasy

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Notifications
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.groeasy.experimental.UpdateProduct
import com.example.groeasy.screens.AddProduct
import com.example.groeasy.screens.HomeScreen
import com.example.groeasy.screens.NotifScreen
import com.example.groeasy.screens.ProductDetailScreen
import com.example.groeasy.screens.ViewProducts
import com.example.groeasy.ui.theme.Green20
import com.example.groeasy.ui.theme.Green40
import com.example.groeasy.ui.theme.GroEasyTheme
import com.example.groeasy.viewmodel.ProductViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        scheduleAlarm(this)
        setContent {
            GroEasyTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    color = MaterialTheme.colorScheme.background,
                    modifier = Modifier.fillMaxSize() // Ensure Surface fills the entire screen
                ) {
                    MyBottomAppBar()
                }

            }
        }
    }

    private fun scheduleAlarm(context: Context) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val intent = Intent(context, AlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)

        // Set the alarm to repeat after 12 hours
        val intervalMillis: Long = 12 * 60 * 60 * 1000 // 12 hours in milliseconds
        val triggerAtMillis = System.currentTimeMillis() + intervalMillis
//        Log.d("Alarm", "Stage 1")
        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            triggerAtMillis,
            intervalMillis,
            pendingIntent
        )
    }
}

@Composable
fun MyBottomAppBar() {

    val navigationController = rememberNavController()
    LocalContext.current.applicationContext
    val selected = remember {
        mutableStateOf(Icons.Rounded.Home)
    }
    val sharedViewProducts: ProductViewModel = viewModel()




    Scaffold(
        bottomBar = {
            BottomAppBar(
                containerColor = Green40,
                modifier = Modifier.clip(RoundedCornerShape(20.dp, 20.dp, 0.dp, 0.dp))
            ) {
                IconButton(
                    onClick = {
                        selected.value = Icons.Rounded.Home
                        navigationController.navigate(Screens.HomeScreen.screen) {
                            popUpTo(0)
                        }
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Home,
                        contentDescription = null,
                        Modifier.size(26.dp),
                        tint = if (selected.value == Icons.Rounded.Home) Color.White else Color.DarkGray
                    )
                }
                Box {
                    FloatingActionButton(
                        containerColor = Green20,
                        onClick = {
                            navigationController.navigate(Screens.AddProduct.screen) {
                                popUpTo(0)
                            }
                        }) {
                        Icon(
                            imageVector = Icons.Rounded.Add,
                            contentDescription = null,
                            Modifier.size(35.dp),
                            tint = Color.DarkGray
                        )
                    }
                }
                IconButton(
                    onClick = {
                        selected.value = Icons.Rounded.Notifications
                        navigationController.navigate(Screens.NotifScreen.screen) {
                            popUpTo(0)
                        }
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Notifications,
                        contentDescription = null,
                        Modifier.size(26.dp),
                        tint = if (selected.value == Icons.Rounded.Notifications) Color.White else Color.DarkGray
                    )
                }
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = navigationController,
            startDestination = Screens.HomeScreen.screen,
            modifier = Modifier.padding(paddingValues)
        ) {
            composable(Screens.HomeScreen.screen) {
                HomeScreen(
                    navigationController,
                    sharedViewProducts
                )
            }
            composable(Screens.NotifScreen.screen) { NotifScreen() }
            composable(Screens.AddProduct.screen) { AddProduct() }
            composable(Screens.ViewProducts.screen + "/{data}") { backStackEntry ->
                val data = backStackEntry.arguments?.getString("data")
                ViewProducts(data, navigationController, sharedViewProducts)
            }
            composable(Screens.ProductDetailScreen.screen) {
                ProductDetailScreen(
                    sharedViewProducts
                )
            }
            composable(Screens.ProductUpdateScreen.screen) {
                UpdateProduct(
                    sharedViewProducts
                )
            }
        }
    }
}







