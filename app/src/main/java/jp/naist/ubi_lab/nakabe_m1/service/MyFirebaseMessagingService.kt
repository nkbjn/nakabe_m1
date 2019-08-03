package jp.naist.ubi_lab.nakabe_m1.service

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import java.lang.NullPointerException

class MyFirebaseMessagingService : FirebaseMessagingService() {
    override fun onNewToken(token: String?) {
        // 端末＋アプリを一意に識別するためのトークンを取得
        Log.i("FIREBASE", "[SERVICE] Token = ${token ?: "Empty"}")
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage?) {
        remoteMessage?.let { message ->
            // 通知メッセージ
            message.notification?.let {notification ->
                // 通知メッセージを処理
                Log.i("FIREBASE2","[Service]  = Title = ${notification.title ?: "Empty2"}")
                Log.i("FIREBASE3","[Service]  = body = ${notification.body ?: "Empty3"}")
            }

            if (message.data == null) {
                // データメッセージ
                message.data?.let { data ->
                    // データメッセージを処理
                    Log.i("FIREBASE4", "[Service]  = Nick = $data")
                    Log.i("FIREBASE5", "[Service]  = Room = $data")
                }
            }else Log.i("FIREBASE6", "空だぜ")
        }
    }
}
