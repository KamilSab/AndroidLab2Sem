package ru.itis.androidlab2sem.data.firebase.messaging

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.messaging.RemoteMessage
import ru.itis.androidlab2sem.R
import ru.itis.androidlab2sem.data.firebase.config.RemoteConfigManager
import ru.itis.androidlab2sem.presentation.common.AppLifecycleTracker
import ru.itis.androidlab2sem.presentation.main.MainActivity
import java.util.Random
import javax.inject.Inject

class FcmMessageHandler @Inject constructor(
    private val context: Context,
    private val notificationManager: NotificationManager,
    private val preferences: SharedPreferences,
    private val appLifecycleTracker: AppLifecycleTracker,
    private val remoteConfigManager: RemoteConfigManager
) {

    private fun isUserLoggedIn(): Boolean {
        return preferences.getBoolean("is_logged_in", false)
    }

    fun handleMessage(message: RemoteMessage) {
        when (message.data["category"]) {
            "notification" -> handleNotification(message)
            "silent" -> handleSilent(message)
            "feature" -> handleFeature(message)
            else -> FirebaseCrashlytics.getInstance()
                .log("Unknown FCM category: ${message.data["category"]}")
        }
    }

    private fun handleNotification(message: RemoteMessage) {
        val title = message.data["title"] ?: "New Cat Alert"
        val content = message.data["message"] ?: "Check out this cute cat!"

        showNotification(title, content)
    }

    private fun showNotification(title: String, content: String) {
        val channelId = "cat_channel_high"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Important Cat Notifications",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Channel for important cat alerts"
            }
            notificationManager.createNotificationChannel(channel)
        }

        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val notification = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.ic_cat_notification)
            .setContentTitle(title)
            .setContentText(content)
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(Random().nextInt(), notification)
    }

    private fun handleSilent(message: RemoteMessage) {
        val data = message.data["data"] ?: return
        preferences.edit().putString("last_silent_data", data).apply()
    }

    private fun handleFeature(message: RemoteMessage) {
        if (!appLifecycleTracker.isAppInForeground) return

        val featureName = message.data["feature"] ?: return
        val isFeatureEnabled = remoteConfigManager.isFeatureEnabled(featureName)

        if (!isFeatureEnabled) {
            showToast("This feature is currently unavailable")
            return
        }

        if (!isUserLoggedIn()) {
            showToast("Please login to access this feature")
            return
        }

        Intent("OPEN_FEATURE").apply {
            putExtra("feature_name", featureName)
            LocalBroadcastManager.getInstance(context).sendBroadcast(this)
        }
    }

    private fun showToast(message: String) {
        Intent("SHOW_TOAST").apply {
            putExtra("toast_message", message)
            LocalBroadcastManager.getInstance(context).sendBroadcast(this)
        }
    }
}