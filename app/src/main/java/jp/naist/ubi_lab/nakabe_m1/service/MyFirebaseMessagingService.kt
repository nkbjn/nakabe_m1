package jp.naist.ubi_lab.nakabe_m1.service

import android.content.Intent
import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import jp.naist.ubi_lab.nakabe_m1.constants.StringConstants.BROADCAST_EMOTION


class MyFirebaseMessagingService : FirebaseMessagingService() {
    var score = 0.0
    var message = ""

    override fun onNewToken(token: String?) {
        // 端末＋アプリを一意に識別するためのトークンを取得
        Log.i("FIREBASE", "[SERVICE] Token = ${token ?: "Empty"}")
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage?) {
        // 通知メッセージ
        remoteMessage?.notification?.let { notification ->
            // 通知メッセージを処理
            Log.i("FIREBASE2", "[Service]  = Title = ${notification.title ?: "Empty2"}")
            Log.i("FIREBASE3", "[Service]  = body = ${notification.body ?: "Empty3"}")
        }

        // データメッセージ
        if (remoteMessage?.data?.isNotEmpty()!!) {
            remoteMessage.data?.let { data ->
                // データメッセージを処理
                score = data["score"]!!.toDouble()
                message = data["text"]!!.toString()
                //ブロードキャストの送信
                val intent = Intent(BROADCAST_EMOTION)
                intent.putExtra(BROADCAST_EMOTION, score)
                sendBroadcast(intent)
            }
        }
    }
}
