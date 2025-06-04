package ru.itis.androidlab2sem

import android.app.Application
import com.google.firebase.FirebaseApp
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.sessions.dagger.BindsInstance
import com.google.firebase.sessions.dagger.Component
import dagger.hilt.android.HiltAndroidApp
import jakarta.inject.Inject
import ru.itis.androidlab2sem.data.firebase.crashlytics.CrashReporting
import ru.itis.androidlab2sem.data.firebase.messaging.CatMessagingService
import ru.itis.androidlab2sem.di.FirebaseModule
import ru.itis.androidlab2sem.presentation.common.AppLifecycleTracker

@HiltAndroidApp
class HiltApplication : Application() {
    val appLifecycleTracker = AppLifecycleTracker()

    @Inject
    lateinit var crashReporting: CrashReporting

    override fun onCreate() {
        super.onCreate()

        registerActivityLifecycleCallbacks(appLifecycleTracker)

        setupFirebase()
    }

    private fun setupFirebase() {
        FirebaseApp.initializeApp(this)

        FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(!BuildConfig.DEBUG)

        crashReporting.logUser("anonymous_user")
    }
}