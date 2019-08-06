package jp.naist.ubi_lab.nakabe_m1.activities

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import jp.naist.ubi_lab.nakabe_m1.R
import jp.naist.ubi_lab.nakabe_m1.constants.StringConstants
import jp.naist.ubi_lab.nakabe_m1.constants.StringConstants.USER_DATA_NAME
import jp.naist.ubi_lab.nakabe_m1.entities.MessageEntity
import jp.naist.ubi_lab.nakabe_m1.services.MyFirebaseMessagingService
import jp.naist.ubi_lab.nakabe_m1.services.OverlayService
import kotlinx.android.synthetic.main.settings_activity.*

class SettingsActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.i("FIREBASE", "ここまで来たよ4-1")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_activity)
        Log.i("FIREBASE", "ここまで来たよ4-2")
        val pref = getSharedPreferences(StringConstants.USER_DATA, Context.MODE_PRIVATE)
        Log.i("FIREBASE", "ここまで来たよ4-3")
        val savedName = pref.getString(USER_DATA_NAME, "")
        Log.i("FIREBASE", "ここまで来たよ4-4")
        if(savedName != ""){
            Log.i("FIREBASE", "ここまで来たよ4-5")
            start_setting_text.setText(savedName)
            Log.i("FIREBASE", "ここまで来たよ4-6")
        }

        finish_setting_button.setOnClickListener {
            //Log.i("FIREBASE", "ここまで来たよ5")
            val messagingIntent = Intent(this, OverlayService::class.java)
            stopService(messagingIntent)
            finishAndRemoveTask()
        }

        start_setting_button.setOnClickListener {
            Log.i("FIREBASE", "ここまで来たよ6")
            val inputName = start_setting_text.text.toString()
            if(inputName == "") start_setting_text.hint = "ターゲットユーザの入力は必須です"
            else {
                Log.i("FIREBASE","inputName = $inputName")
                pref.edit().putString(USER_DATA_NAME, inputName).apply()
                val overlayIntent = Intent(this, OverlayService::class.java)
                startService(overlayIntent)
                val messagingIntent = Intent(this, FirebaseMessagingService::class.java)
                startService(messagingIntent)
                finish()
            }
        }

        // Broadcastの動作確認用
        sad_setting_button.setOnClickListener {
            Log.i("FIREBASE", "ここまで来たよ8")
            val currentName = pref.getString(USER_DATA_NAME, "")!!
            val intent = Intent(StringConstants.BROADCAST_EMOTION)
            intent.putExtra(StringConstants.BROADCAST_EMOTION, MessageEntity(-1.0, currentName))
            sendBroadcast(intent)
        }

        normal_setting_button.setOnClickListener {
            Log.i("FIREBASE", "ここまで来たよ9")
            val currentName = pref.getString(USER_DATA_NAME, "")!!
            val intent = Intent(StringConstants.BROADCAST_EMOTION)
            intent.putExtra(StringConstants.BROADCAST_EMOTION, MessageEntity(0.0, currentName))
            sendBroadcast(intent)
        }

        happy_setting_button.setOnClickListener {
            Log.i("FIREBASE", "ここまで来たよ10")
            val currentName = pref.getString(USER_DATA_NAME, "")!!
            val intent = Intent(StringConstants.BROADCAST_EMOTION)
            intent.putExtra(StringConstants.BROADCAST_EMOTION, MessageEntity(1.0, currentName))
            sendBroadcast(intent)
        }

    }
}
