package com.example.groeasy

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.groeasy.screens.sendNotification

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (context != null) {
            sendNotification(context)
//            Log.d("Alarm", "onReceive: context got")
        } else {
//            Log.d("Alarm", "onReceive works")
        }
        // Increment your counter here
    }

}
