package com.nsp.timetracker.support.extensions

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.nsp.timetracker.R
import com.nsp.timetracker.base.Constants
import com.nsp.timetracker.base.Constants.Companion.CHANNEL_ID
import kotlin.random.Random

fun Context.sendNotification(title: String, description: String) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        val importance = NotificationManager.IMPORTANCE_HIGH
        val channel = NotificationChannel(CHANNEL_ID, "CHANNEL_NAME", importance)
        channel.description = description

        // Add the channel
        val notificationManager =
            this.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager?
        notificationManager?.createNotificationChannel(channel)
    }

    val builder = NotificationCompat.Builder(this, Constants.CHANNEL_ID)
        .setContentTitle(title)
        .setContentText(description)
        .setSmallIcon(R.drawable.ic_launcher_foreground)
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
    with(NotificationManagerCompat.from(this)) {
        notify(Random.nextInt(), builder.build())
    }
}