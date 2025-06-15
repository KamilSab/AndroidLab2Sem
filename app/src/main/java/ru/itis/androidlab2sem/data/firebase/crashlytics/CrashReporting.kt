package ru.itis.androidlab2sem.data.firebase.crashlytics

import com.google.firebase.crashlytics.FirebaseCrashlytics
import jakarta.inject.Inject

class CrashReporting @Inject constructor() {
    fun logUser(userId: String) {
        FirebaseCrashlytics.getInstance().setUserId(userId)
    }

    fun logCustomKey(key: String, value: String) {
        FirebaseCrashlytics.getInstance().setCustomKey(key, value)
    }

    fun logException(exception: Throwable) {
        FirebaseCrashlytics.getInstance().recordException(exception)
    }
}