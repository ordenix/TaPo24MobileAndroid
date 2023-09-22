package pl.tapo24.twa

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class FireBase: FirebaseMessagingService() {

    override fun onMessageReceived(message: RemoteMessage) {
        println("sssssssssssssss")
        println(message.notification?.title)
        super.onMessageReceived(message)
    }

    override fun onNewToken(token: String) {
        println("NEWWWWW")
    }
    // https://medium.com/@Codeible/android-notifications-with-firebase-cloud-messaging-914623716dea
}