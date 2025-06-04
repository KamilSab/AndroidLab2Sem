package ru.itis.androidlab2sem.domain.model

sealed class FcmMessage(val category: String) {
    data class NotificationMessage(
        val title: String,
        val content: String
    ) : FcmMessage("notification")

    data class SilentMessage(
        val data: String
    ) : FcmMessage("silent")

    data class FeatureMessage(
        val featureName: String
    ) : FcmMessage("feature")
}