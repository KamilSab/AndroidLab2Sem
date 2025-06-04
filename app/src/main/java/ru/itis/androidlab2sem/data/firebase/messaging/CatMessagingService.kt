package ru.itis.androidlab2sem.data.firebase.messaging

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import jakarta.inject.Inject
import ru.itis.androidlab2sem.HiltApplication

class CatMessagingService : FirebaseMessagingService() {
    @Inject
    lateinit var fcmMessageHandler: FcmMessageHandler

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        fcmMessageHandler.handleMessage(message)
    }
}