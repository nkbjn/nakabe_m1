package jp.naist.ubi_lab.nakabe_m1.service

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.PixelFormat
import android.graphics.Point
import android.os.Build
import android.os.IBinder
import android.view.*
import android.view.WindowManager
import jp.naist.ubi_lab.nakabe_m1.R
import jp.naist.ubi_lab.nakabe_m1.activity.SettingsActivity
import jp.naist.ubi_lab.nakabe_m1.constants.StringConstants.BROADCAST_EMOTION
import jp.naist.ubi_lab.nakabe_m1.constants.ValueConstants.EMOTION_THRESHOLD_HAPPY
import jp.naist.ubi_lab.nakabe_m1.constants.ValueConstants.EMOTION_THRESHOLD_SAD
import kotlinx.android.synthetic.main.overlay_layout.view.*




@SuppressLint("InflateParams")
class OverlayService : Service() {
    private val overlayView: ViewGroup by lazy {
        LayoutInflater.from(this).inflate(
            R.layout.overlay_layout,
            null
        ) as ViewGroup
    }

    private lateinit var receiver: BroadcastReceiver
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

    override fun onCreate() {
        super.onCreate()
        receiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                if (intent?.action.equals(BROADCAST_EMOTION)) {
                    val score = intent?.getDoubleExtra(BROADCAST_EMOTION, 0.0)!!
                    changeEmotionImage(score)
                }
            }
        }

        val intentFilter = IntentFilter()
        intentFilter.addAction(BROADCAST_EMOTION)
        registerReceiver(receiver, intentFilter)
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

    override fun onDestroy() {
        super.onDestroy()
        windowManager.removeView(overlayView)
        unregisterReceiver(receiver)
    }

    private fun clickListener(): View.() -> Unit {
        return {
            setOnLongClickListener {
                isLongClick = true
                true
            }.apply {
                setOnTouchListener { view, motionEvent ->
                    val x = motionEvent.rawX.toInt()
                    val y = motionEvent.rawY.toInt()
                    when (motionEvent.action) {
                        MotionEvent.ACTION_MOVE -> {
                            if (isLongClick) {
                                overlayView.emotion_image.imageAlpha = 100
                                val centerX = x - (displaySize.x / 2)
                                val centerY = y - (displaySize.y / 2)
                                params?.x = centerX
                                params?.y = centerY
                                windowManager.updateViewLayout(overlayView, params)
                            }
                        }
                        MotionEvent.ACTION_UP -> {
                            overlayView.emotion_image.imageAlpha = 255
                            if (isLongClick) {
                                view.setBackgroundResource(android.R.color.transparent)
                            }
                            isLongClick = false
                        }
                    }
                    false
                }
            }

            setOnClickListener {
                val intent = Intent(this@OverlayService, SettingsActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                application.startActivity(intent)
            }
        }
    }

    private fun changeEmotionImage(score: Double) {
        overlayView.emotion_score.text = score.toString()

        when{
            score < EMOTION_THRESHOLD_SAD ->{
                overlayView.emotion_image.setImageResource(R.drawable.ic_face_sad)
            }

            score > EMOTION_THRESHOLD_HAPPY ->{
                overlayView.emotion_image.setImageResource(R.drawable.ic_face_happy)
            }

            else ->{
                overlayView.emotion_image.setImageResource(R.drawable.ic_face_normal)
            }
        }
    }
}
