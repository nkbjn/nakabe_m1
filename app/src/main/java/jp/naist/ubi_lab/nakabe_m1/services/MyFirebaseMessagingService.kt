package jp.naist.ubi_lab.nakabe_m1.services

import android.content.Intent
import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import jp.naist.ubi_lab.nakabe_m1.constants.StringConstants.BROADCAST_EMOTION
import jp.naist.ubi_lab.nakabe_m1.entities.MessageEntity


class MyFirebaseMessagingService : FirebaseMessagingService() {
    var messagingEntity: MessageEntity = MessageEntity()

    override fun onNewToken(token: String?) {
        // 端末＋アプリを一意に識別するためのトークンを取得
        //Log.i("FIREBASE", "ここまで来たよ11")
        Log.i("FIREBASE", "[SERVICE] Token = ${token ?: "Empty"}")
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage?) {
        // 通知メッセージ
        /*remoteMessage?.notification?.let { notification ->
            // 通知メッセージを処理
            Log.i("FIREBASE2", "[Service]  = Title = ${notification.title ?: "Empty2"}")
            Log.i("FIREBASE3", "[Service]  = body = ${notification.body ?: "Empty3"}")
        }*/
        //Log.i("FIREBASE", "ここまで来たよ12")
        // データメッセージ
        if (remoteMessage?.data?.isNotEmpty()!!) {
            remoteMessage.data?.let { data ->
                Log.i("FIREBASE", "user = "+data.getValue("user").toString())
                //Log.i("FIREBASE", "score = body = ${data.score ?: "Empty2"}")
                //Log.i("FIREBASE", "text = ${data.text ?: "Empty3"}")
                // データメッセージを処理
                messagingEntity = MessageEntity(data)
                //ブロードキャストの送信
                val intent = Intent(BROADCAST_EMOTION)
                intent.putExtra(BROADCAST_EMOTION, messagingEntity)
                sendBroadcast(intent)
            }
        }
    }
}
