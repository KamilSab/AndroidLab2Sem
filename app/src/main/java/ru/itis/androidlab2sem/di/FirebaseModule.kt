package ru.itis.androidlab2sem.di

import android.app.NotificationManager
import android.content.Context
import android.content.SharedPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Singleton
import ru.itis.androidlab2sem.data.firebase.config.RemoteConfigManager
import ru.itis.androidlab2sem.data.firebase.messaging.FcmMessageHandler
import ru.itis.androidlab2sem.presentation.common.AppLifecycleTracker

@Module
@InstallIn(SingletonComponent::class)
object FirebaseModule {

    @Provides
    @Singleton
    fun provideNotificationManager(
        @ApplicationContext context: Context
    ): NotificationManager {
        return context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }

    @Provides
    @Singleton
    fun provideSharedPreferences(
        @ApplicationContext context: Context
    ): SharedPreferences {
        return context.getSharedPreferences(
            "AppPrefs",
            Context.MODE_PRIVATE
        )
    }

    @Provides
    @Singleton
    fun provideFcmMessageHandler(
        @ApplicationContext context: Context,
        notificationManager: NotificationManager,
        preferences: SharedPreferences,
        appLifecycleTracker: AppLifecycleTracker,
        remoteConfigManager: RemoteConfigManager
    ): FcmMessageHandler {
        return FcmMessageHandler(
            context,
            notificationManager,
            preferences,
            appLifecycleTracker,
            remoteConfigManager
        )
    }
}