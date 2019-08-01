package jp.naist.ubi_lab.nakabe_m1.service;

import android.util.Log;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

class MessagingService extends FirebaseMessagingService {

    @Override
    public void onNewToken(String token) {
        // 端末＋アプリを一意に識別するためのトークンを取得
        Log.i("FIREBASE", "[SERVICE] Token = " + token);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        if (remoteMessage != null) {
            // 通知メッセージ
            RemoteMessage.Notification notification = remoteMessage.getNotification();
            if (notification != null) {
                // 通知メッセージを処理
            }

            // データメッセージ
            Map<String, String> data = remoteMessage.getData();
            if (data != null) {
                // データメッセージを処理
            }
        }
    }
}
