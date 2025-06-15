package ru.itis.androidlab2sem.presentation.common

import android.app.Activity
import android.app.Application
import android.os.Bundle

class AppLifecycleTracker : Application.ActivityLifecycleCallbacks {

    private var activityCount = 0
    var isAppInForeground: Boolean = false
        private set

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {}

    override fun onActivityStarted(activity: Activity) {
        if (activityCount++ == 0) {
            isAppInForeground = true
        }
    }

    override fun onActivityResumed(activity: Activity) {}

    override fun onActivityPaused(activity: Activity) {}

    override fun onActivityStopped(activity: Activity) {
        if (--activityCount == 0) {
            isAppInForeground = false
        }
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}

    override fun onActivityDestroyed(activity: Activity) {}
}