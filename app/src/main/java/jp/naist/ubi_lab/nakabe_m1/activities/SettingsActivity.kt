package jp.naist.ubi_lab.nakabe_m1.activities

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import com.google.firebase.messaging.FirebaseMessagingService
import jp.naist.ubi_lab.nakabe_m1.R
import jp.naist.ubi_lab.nakabe_m1.constants.StringConstants
import jp.naist.ubi_lab.nakabe_m1.constants.StringConstants.USER_DATA_NAME
import jp.naist.ubi_lab.nakabe_m1.constants.ValueConstants
import jp.naist.ubi_lab.nakabe_m1.entities.MessageEntity
import jp.naist.ubi_lab.nakabe_m1.services.OverlayService
import kotlinx.android.synthetic.main.settings_activity.*

class SettingsActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_activity)
        val pref = getSharedPreferences(StringConstants.USER_DATA, Context.MODE_PRIVATE)
        val savedName = pref.getString(USER_DATA_NAME, "")
        if(savedName != "") start_setting_text.setText(savedName)

        finish_setting_button.setOnClickListener {
            val messagingIntent = Intent(this, OverlayService::class.java)
            stopService(messagingIntent)
            finishAndRemoveTask()
        }

        start_setting_button.setOnClickListener {
            val inputName = start_setting_text.text.toString()
            if(inputName == "") start_setting_text.hint = "ターゲットユーザの入力は必須です"
            else {
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
            val currentName = pref.getString(USER_DATA_NAME, "")!!
            val intent = Intent(StringConstants.BROADCAST_EMOTION)
            intent.putExtra(StringConstants.BROADCAST_EMOTION, MessageEntity(-1.0, currentName))
            sendBroadcast(intent)
        }

        normal_setting_button.setOnClickListener {
            val currentName = pref.getString(USER_DATA_NAME, "")!!
            val intent = Intent(StringConstants.BROADCAST_EMOTION)
            intent.putExtra(StringConstants.BROADCAST_EMOTION, MessageEntity(0.0, currentName))
            sendBroadcast(intent)
        }

        happy_setting_button.setOnClickListener {
            val currentName = pref.getString(USER_DATA_NAME, "")!!
            val intent = Intent(StringConstants.BROADCAST_EMOTION)
            intent.putExtra(StringConstants.BROADCAST_EMOTION, MessageEntity(1.0, currentName))
            sendBroadcast(intent)
        }

    }
}
