package jp.naist.ubi_lab.nakabe_m1.activities

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import jp.naist.ubi_lab.nakabe_m1.constants.StringConstants
import jp.naist.ubi_lab.nakabe_m1.constants.StringConstants.USER_DATA_NAME
import jp.naist.ubi_lab.nakabe_m1.constants.ValueConstants.CUSTOM_OVERLAY_PERMISSION_REQUEST_CODE
import jp.naist.ubi_lab.nakabe_m1.services.OverlayService
import java.lang.Thread.sleep

class MainActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        //Log.i("FIREBASE", "ここまで来たよ0")
        super.onCreate(savedInstanceState)
        checkOverlay()
        finish()
        val pref = getSharedPreferences(StringConstants.USER_DATA, Context.MODE_PRIVATE)
        val currentName = pref.getString(USER_DATA_NAME, "")
        if(currentName == ""){
            //Log.i("FIREBASE", "ここまで来たよ1")
            val settingsIntent = Intent(this, SettingsActivity::class.java)
            startActivity(settingsIntent)
            finish()
        } else {
            //Log.i("FIREBASE", "ここまで来たよ2")
            //checkOverlay()
            val overlayIntent = Intent(this, OverlayService::class.java)
            startService(overlayIntent)
            val messagingIntent = Intent(this, FirebaseMessagingService::class.java)
            startService(messagingIntent)
            finish()
        }
    }

    private fun checkOverlay() {
        //Log.i("FIREBASE", "ここまで来たよ3")
            if (!Settings.canDrawOverlays(this)) {
                val intent =
                    Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + this.packageName))
                startActivityForResult(
                    intent,
                    CUSTOM_OVERLAY_PERMISSION_REQUEST_CODE
                )
                while(!Settings.canDrawOverlays(this)){
                    sleep(100)
                }
                //Log.i("FIREBASE","あいう")
            }
        }
}
