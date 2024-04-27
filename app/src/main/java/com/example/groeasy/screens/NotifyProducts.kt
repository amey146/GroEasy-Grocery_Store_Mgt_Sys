package com.example.groeasy.screens

import android.app.Activity
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.example.groeasy.R
import com.example.groeasy.model.DatabaseHelper
import com.example.groeasy.model.Product
import java.text.DateFormat
import java.time.LocalDate
import java.time.ZoneOffset
import java.util.Date
import java.util.Locale

fun sendNotification(context: Context) {
    val dbhelper = DatabaseHelper.getDb(context)
    val productDao = dbhelper.productDao()

    val today = LocalDate.now()
    val upcoming = today.plusDays(1)

    val todayInMillis = today.atStartOfDay().toInstant(ZoneOffset.UTC).toEpochMilli()
    val upcomingInMillis = upcoming.atStartOfDay().toInstant(ZoneOffset.UTC).toEpochMilli()
    val products: List<Product> = productDao.getNotifiedExpiry(todayInMillis, upcomingInMillis)
    if (products.isEmpty()) {
        return
    }
    // Check permission for Android 13 and above
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
        ContextCompat.checkSelfPermission(
            context,
            android.Manifest.permission.POST_NOTIFICATIONS
        ) != PackageManager.PERMISSION_GRANTED
    ) {
        requestPostNotificationPermission(context)
    }

    var notificationManager: NotificationManager? = null

    if (notificationManager == null) {
        notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        createNotificationChannel(context) // Call only once
//        Log.d("Notification", "Stage 1")
    }
    var message: String
    if (products.size == 1) {
        val prod = products[0].name
        val date = Date(products[0].expiry)

        val formatter = DateFormat.getDateInstance(
            DateFormat.MEDIUM,
            Locale.getDefault()
        )

        val formattedDate = formatter.format(date)
        message =
            "Attention! The $prod is nearing its expiration date. Take action now to ensure freshness and safety.\nExpiry: $formattedDate"
    } else {
        message =
            "Attention! You have multiple products nearing their expiration dates. Take action now to ensure freshness and safety. Here's a list of products that require your attention: \n"
        for (i in products.indices) {
            val product = products[i]
            val date = Date(product.expiry)

            val formatter = DateFormat.getDateInstance(
                DateFormat.MEDIUM,
                Locale.getDefault()
            )

            val formattedDate = formatter.format(date)
            message += "${product.name}: $formattedDate\n"
        }
    }

    buildNotification(
        context,
        "PRODUCT EXPIRY ALERT!!",
        message
    )
//    Log.d("Notification", "Stage 2")
    notificationManager.notify(1, buildNotification(context, "PRODUCT EXPIRY ALERT!!", message))

//    Log.d("Notification", "Stage 3")
}

fun createNotificationChannel(context: Context) {
    val channelId = "1"
    val name = "Alerts"
    val importance = NotificationManager.IMPORTANCE_HIGH

    val channel = NotificationChannel(channelId, name, importance)
        .apply {
            description = "Alerts for products expiry"
        }

    val notificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    notificationManager.createNotificationChannel(channel)
}
fun buildNotification(
    context: Context,
    title: String,
    message: String
): Notification {
    val channelId = "1" // Replace with your channel ID
    val smallIcon = R.drawable.warning // Replace 'good' with the appropriate icon for your small notification icon
    val largeIcon = R.drawable.decor // Replace 'decor' with the appropriate icon for your large notification icon

    return NotificationCompat.Builder(context, channelId)
        .setSmallIcon(smallIcon)
        .setContentTitle(title)
        .setContentText(message)
        .setLargeIcon(BitmapFactory.decodeResource(context.resources, largeIcon))
        .setStyle(NotificationCompat.BigTextStyle().bigText(message))
        .build()
}


@RequiresApi(Build.VERSION_CODES.TIRAMISU)
private fun requestPostNotificationPermission(context: Context) {
    ActivityCompat.requestPermissions(
        context as Activity,
        arrayOf(android.Manifest.permission.POST_NOTIFICATIONS),
        REQUEST_CODE_POST_NOTIFICATION
    )
}

private const val REQUEST_CODE_POST_NOTIFICATION = 101 // Choose a unique code
