package com.apps.alan.dogs.util

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.apps.alan.dogs.MainActivity
import com.apps.alan.dogs.R

class NotificationsHelper(val context: Context) {

    private val CHANEL_ID = "Dogs channel id"
    private val NOTIFICATION_ID = 123

    fun createNotification(){
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val pendingIntent = PendingIntent.getActivity(context, 0, intent, 0)

        val icon = BitmapFactory.decodeResource(context.resources, R.mipmap.ic_dog_icon)

        val notification = NotificationCompat.Builder(context, CHANEL_ID)
            .setSmallIcon(R.mipmap.ic_dog_icon)
            .setLargeIcon(icon)
            .setContentTitle("Dogs retrieved")
            .setContentText("This notification has some content")
            .setStyle(
                NotificationCompat.BigPictureStyle()
                    .bigPicture(icon)
                    .bigLargeIcon(null)
            )
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()

        NotificationManagerCompat.from(context).notify(NOTIFICATION_ID, notification)

    }

    private fun createNotficationChannel(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val name = CHANEL_ID
            val descriptionTExt = "Channel description"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val chanel = NotificationChannel(CHANEL_ID, name, importance).apply {
                description = descriptionTExt
            }
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(chanel)
        }
    }
}