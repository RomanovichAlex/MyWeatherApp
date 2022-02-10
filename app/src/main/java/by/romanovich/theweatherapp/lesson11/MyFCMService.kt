package by.romanovich.theweatherapp.lesson11

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import by.romanovich.theweatherapp.R
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFCMService : FirebaseMessagingService() {
    override fun onNewToken(s: String) {
        super.onNewToken(s)
        Log.d("mylogs", "token $s")
    }


    //приходит сообщение в нашу дата
    override fun onMessageReceived(message: RemoteMessage) {
        val data = message.data.toMap()
        if(data.isNotEmpty()){
            //в нашей дате по ключам сидят значения
            val title =data[KEY_TITLE]
            val message =data[KEY_MESSAGE]
            if(!title.isNullOrBlank()&&!message.isNullOrBlank())
                pushNotification(title,message)
        }
    }


    companion object {
        private const val NOTIFICATION_ID = 1
        private const val CHANNEL_ID = "channel_id_1"
        private const val KEY_TITLE = "myTitle"
        private const val KEY_MESSAGE = "myMessage"
    }

    //будем получать с сервера title и message и будем их отображать
    private fun pushNotification(title:String,message:String) {
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notificationBuilder = NotificationCompat.Builder(this,CHANNEL_ID).apply {
            setSmallIcon(R.drawable.ic_kotlin_logo)
            setContentTitle(title)
            setContentText(message)
            priority = NotificationCompat.PRIORITY_MAX
        }
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val channelName_1 = "Name $CHANNEL_ID"
            val channelDescription_1 = "Description for $CHANNEL_ID"
            val channelPriority_1 = NotificationManager.IMPORTANCE_HIGH

            val channel = NotificationChannel(CHANNEL_ID,channelName_1,channelPriority_1).apply {
                description = channelDescription_1
            }
            notificationManager.createNotificationChannel(channel)
        }
        notificationManager.notify(NOTIFICATION_ID,notificationBuilder.build())
    }
}



//Server key
//AAAAXoOkCPM:APA91bGVRVCwV9DQdrP3Mzki7ZqoyQtAPe4zmPxIP7hSNwZ-WtcgbfzVFOKQHNdEly54myl_tTWd5VnO-stAkq06f8Fx5PDyHIgXuDi9z3K1gnajV8LOB9_csNTv24RR0Psp_l1giJ_o


//eJ8nase3TC21r7LaIIhM_I:APA91bF-NTlm1So6784SWu3jsBnN6CFX56rvJIMCleiqvbPX_YdTHiJE59jBUHG8eA6QHqKao2ThPxNgd8G-VoOrfiFj9de7Kav4SC8Ts0ICGUcXCwRvdFrvM5z_rKKE-64WB30j6kst