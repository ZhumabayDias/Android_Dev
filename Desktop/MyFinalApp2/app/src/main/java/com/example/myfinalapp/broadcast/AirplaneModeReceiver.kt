package com.example.myfinalapp.broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.provider.Settings
import android.widget.Toast

class AirplaneModeReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_AIRPLANE_MODE_CHANGED) {
            val isAirplaneModeOn = Settings.Global.getInt(
                context.contentResolver,
                Settings.Global.AIRPLANE_MODE_ON, 0
            ) != 0

            val message = if (isAirplaneModeOn) "Airplane mode is turned on" else "Airplane mode is turned off"
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }
}