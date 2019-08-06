package jp.naist.ubi_lab.nakabe_m1.services

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.Service
import android.content.*
import android.graphics.PixelFormat
import android.graphics.Point
import android.os.Build
import android.os.IBinder
import android.util.Log
import android.view.*
import android.view.WindowManager
import jp.naist.ubi_lab.nakabe_m1.R
import jp.naist.ubi_lab.nakabe_m1.activities.SettingsActivity
import jp.naist.ubi_lab.nakabe_m1.constants.StringConstants.BROADCAST_EMOTION
import jp.naist.ubi_lab.nakabe_m1.constants.StringConstants.USER_DATA
import jp.naist.ubi_lab.nakabe_m1.constants.StringConstants.USER_DATA_NAME
import jp.naist.ubi_lab.nakabe_m1.constants.ValueConstants.EMOTION_THRESHOLD_HAPPY
import jp.naist.ubi_lab.nakabe_m1.constants.ValueConstants.EMOTION_THRESHOLD_SAD
import jp.naist.ubi_lab.nakabe_m1.entities.MessageEntity
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
        //Log.i("FIREBASE", "ここまで来たよ13-1")
        return null
    }

    override fun onCreate() {
        super.onCreate()
        //Log.i("FIREBASE", "ここまで来たよ13-2")
        receiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                //Log.i("FIREBASE", "ここまで来たよ13-3")
                if (intent?.action.equals(BROADCAST_EMOTION)) {
                    Log.i("FIREBASE", "ここまで来たよ13-4")
                    val data = intent?.getSerializableExtra(BROADCAST_EMOTION)!!
                    //Log.i("FIREBASE", "ここまで来たよ13-5")
                    if(data is MessageEntity) changeEmotionImage(data.score, data.user)
                    //Log.i("FIREBASE", "ここまで来たよ13-6")
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

    private fun changeEmotionImage(score: Double, user: String) {
        if(filterByUser(user)) {//追加
            overlayView.emotion_score.text = score.toString()
            when {
                score < EMOTION_THRESHOLD_SAD -> {
                    overlayView.emotion_image.setImageResource(R.drawable.ic_face_sad)
                }

                score > EMOTION_THRESHOLD_HAPPY -> {
                    overlayView.emotion_image.setImageResource(R.drawable.ic_face_happy)
                }

                else -> {
                    overlayView.emotion_image.setImageResource(R.drawable.ic_face_normal)
                }
            }
        }
    }

    private fun filterByUser(user: String): Boolean {
        val pref = getSharedPreferences(USER_DATA, Context.MODE_PRIVATE)
        val currentName = pref.getString(USER_DATA_NAME, "")
        Log.i("FIREBASE","currentName = $currentName, user = $user")
        return currentName != user
        /*
        return if (currentName == "") false
        else currentName == user
        */
    }
}
