package ru.itis.androidlab2sem.di

import android.app.Application
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Singleton
import ru.itis.androidlab2sem.HiltApplication
import ru.itis.androidlab2sem.presentation.common.AppLifecycleTracker

@Module
@InstallIn(SingletonComponent::class)
class AppModule {
    @Provides
    @Singleton
    fun provideAppLifecycleTracker(application: HiltApplication): AppLifecycleTracker {
        return application.appLifecycleTracker
    }

    @Provides
    @Singleton
    fun provideAppLifecycleTracker(application: Application): AppLifecycleTracker {
        return (application as HiltApplication).appLifecycleTracker
    }
}