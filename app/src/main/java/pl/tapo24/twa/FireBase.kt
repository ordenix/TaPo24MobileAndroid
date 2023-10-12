package pl.tapo24.twa

import android.Manifest
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class FireBase: FirebaseMessagingService() {

    override fun onMessageReceived(message: RemoteMessage) {
//        println("sssssssssssssss")
//        println(message.notification?.title)

//Bundle[{google.c.a.c_l=TEST!!, google.c.a.udt=0, google.delivered_priority=high, google.sent_time=1695410273022, gcm.notification.android_channel_id=CHANNEL_ID, google.ttl=2419200, google.original_priority=high, gcm.notification.e=1, gcm.n.dnp=1, google.c.a.c_id=5127343841894549226, google.c.a.ts=1695410260, gcm.notification.sound=default, gcm.notification.title=TEST!!, gcm.n.e=1, te=1000, from=268792039367, gcm.notification.sound2=default, google.message_id=0:1695410450786190%f32f1ff0f32f1ff0, gcm.notification.body=SAMPLE, google.c.a.e=1, google.c.sender.id=268792039367, gcm.notification.tag=campaign_collapse_key_5127343841894549226, collapse_key=pl.tapo24.twa}]

        var builder = NotificationCompat.Builder(this, "fcm_fallback_notification_channel")
            .setSmallIcon(R.drawable.woman_news)
            .setContentTitle(message.notification?.title)
            .setContentText(message.notification?.body)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        with(NotificationManagerCompat.from(this)) {
            // notificationId is a unique int for each notification that you must define
            if (ActivityCompat.checkSelfPermission(
                    applicationContext,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return
            }
            notify(1, builder.build())
        }


        super.onMessageReceived(message)
    }

    override fun onNewToken(token: String) {
        println("NEWWWWW")
    }
    // https://medium.com/@Codeible/android-notifications-with-firebase-cloud-messaging-914623716dea
}