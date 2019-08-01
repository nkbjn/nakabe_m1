package jp.naist.ubi_lab.nakabe_m1.service

import android.annotation.TargetApi
import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.PixelFormat
import android.graphics.Point
import android.os.Build
import android.os.IBinder
import android.view.*
import android.view.WindowManager
import jp.naist.ubi_lab.nakabe_m1.R


class OverlayService : Service() {
    private val overlayView: ViewGroup by lazy {
        LayoutInflater.from(this).inflate(
            R.layout.overlay_layout,
            null
        ) as ViewGroup
    }

    private val windowManager: WindowManager by lazy { applicationContext.getSystemService(Context.WINDOW_SERVICE) as WindowManager }
    private var params: WindowManager.LayoutParams? = null
    private val displaySize: Point by lazy {
        val display = windowManager.defaultDisplay
        val size = Point()
        display.getSize(size)
        size
    }

    // ロングタップ判定用
    private var isLongClick: Boolean = false

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    @TargetApi(Build.VERSION_CODES.O)
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        overlayView.apply(clickListener())

        params = WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or
                    WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN or
                    WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL or
                    WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH,
            PixelFormat.TRANSLUCENT
        )
        windowManager.addView(overlayView, params)

        return START_STICKY
    }

    private fun clickListener(): View.() -> Unit {
        return {
            setOnLongClickListener { view ->
                isLongClick = true
                view.setBackgroundResource(R.color.colorSelected)
                false
            }.apply {
                setOnTouchListener { view, motionEvent ->
                    val x = motionEvent.rawX.toInt()
                    val y = motionEvent.rawY.toInt()
                    when (motionEvent.action) {
                        MotionEvent.ACTION_MOVE -> {
                            if (isLongClick) {
                                val centerX = x - (displaySize.x / 2)
                                val centerY = y - (displaySize.y / 2)
                                params?.x = centerX
                                params?.y = centerY
                                windowManager.updateViewLayout(overlayView, params)
                            }
                        }
                        MotionEvent.ACTION_UP -> {
                            if (isLongClick) {
                                view.setBackgroundResource(android.R.color.transparent)
                            }
                            isLongClick = false
                        }
                    }
                    false
                }
            }
        }
    }
}
