package jp.naist.ubi_lab.nakabe_m1

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import jp.naist.ubi_lab.nakabe_m1.service.OverlayService


class MainActivity : Activity() {
    private val CUSTOM_OVERLAY_PERMISSION_REQUEST_CODE = 101

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        checkOverlay()
        val serviceIntent = Intent(this, OverlayService::class.java)
        startService(serviceIntent)
        this.finishAndRemoveTask()
    }

    private fun checkOverlay() {
        if (!Settings.canDrawOverlays(this)){
            val intent =
                Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + this.packageName))
            startActivityForResult(
                intent,
                CUSTOM_OVERLAY_PERMISSION_REQUEST_CODE
            )
        }
    }
}
