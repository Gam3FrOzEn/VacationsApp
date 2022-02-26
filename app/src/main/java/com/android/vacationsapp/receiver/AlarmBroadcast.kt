package com.android.vacationsapp.receiver

import android.annotation.SuppressLint
import android.app.*
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.media.RingtoneManager
import android.os.Build
import android.text.Html
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.android.vacationsapp.R


class AlarmBroadcast : BroadcastReceiver() {

    @RequiresApi(Build.VERSION_CODES.M)
    @SuppressLint("UnspecifiedImmutableFlag")
    override fun onReceive(context: Context, intent: Intent) {

        if (intent.action == "android.intent.action.BOOT_COMPLETED" || intent.action == AlarmManager.ACTION_SCHEDULE_EXACT_ALARM_PERMISSION_STATE_CHANGED) {
            val message = intent.getStringExtra("message")
            val title = intent.getStringExtra("title")
            val code = intent.getIntExtra("code", 0)

            val icon = R.mipmap.ic_launcher
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            val resultPendingIntent: PendingIntent = PendingIntent.getActivity(
                context,
                code,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT
            )
            val mBuilder = NotificationCompat.Builder(
                context, CHANNEL_ID
            )

            //If Bitmap is created from URL, show big icon
            val bigPictureStyle = NotificationCompat.BigPictureStyle()
            bigPictureStyle.setBigContentTitle(title)
            bigPictureStyle.setSummaryText(Html.fromHtml(message).toString())
            val notification: Notification =
                mBuilder.setSmallIcon(icon).setTicker(title)
                    .setAutoCancel(true)
                    .setContentTitle(title)
                    .setChannelId(CHANNEL_ID)
                    .setContentIntent(resultPendingIntent)
                    .setStyle(NotificationCompat.BigTextStyle().bigText(message))
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setLargeIcon(BitmapFactory.decodeResource(context.resources, icon))
                    .setContentText(message)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                    .build()
            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            //All notifications should go through NotificationChannel on Android 26 & above
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val channel = NotificationChannel(
                    CHANNEL_ID,
                    CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_HIGH
                )
                channel.enableLights(true)
                channel.enableVibration(true)
                notificationManager.createNotificationChannel(channel)
            }
            notificationManager.notify(code, notification)
        } else {
            val message = intent.getStringExtra("message")
            val title = intent.getStringExtra("title")
            val code = intent.getIntExtra("code", 0)

            val icon = R.mipmap.ic_launcher
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            val resultPendingIntent: PendingIntent = PendingIntent.getActivity(
                context,
                code,
                intent,
                PendingIntent.FLAG_IMMUTABLE
            )
            val mBuilder = NotificationCompat.Builder(
                context, CHANNEL_ID
            )

            //If Bitmap is created from URL, show big icon
            val bigPictureStyle = NotificationCompat.BigPictureStyle()
            bigPictureStyle.setBigContentTitle(title)
            bigPictureStyle.setSummaryText(Html.fromHtml(message).toString())
            val notification: Notification =
                mBuilder.setSmallIcon(icon).setTicker(title)
                    .setAutoCancel(true)
                    .setContentTitle(title)
                    .setChannelId(CHANNEL_ID)
                    .setContentIntent(resultPendingIntent)
                    .setStyle(NotificationCompat.BigTextStyle().bigText(message))
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setLargeIcon(BitmapFactory.decodeResource(context.resources, icon))
                    .setContentText(message)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                    .build()
            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            //All notifications should go through NotificationChannel on Android 26 & above
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val channel = NotificationChannel(
                    CHANNEL_ID,
                    CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_HIGH
                )
                channel.enableLights(true)
                channel.enableVibration(true)
                notificationManager.createNotificationChannel(channel)
            }
            notificationManager.notify(code, notification)
        }
    }

    companion object {
        private const val CHANNEL_ID = "plantcare"
        private const val CHANNEL_NAME = "myChannelName"
    }
}