package jp.naist.ubi_lab.nakabe_m1.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import jp.naist.ubi_lab.nakabe_m1.R
import jp.naist.ubi_lab.nakabe_m1.service.OverlayService
import kotlinx.android.synthetic.main.settings_activity.*

class SettingsActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_activity)

        finish_setting_button.setOnClickListener {
            val messagingIntent = Intent(this, OverlayService::class.java)
            stopService(messagingIntent)
            finishAndRemoveTask()
        }
    }
}
