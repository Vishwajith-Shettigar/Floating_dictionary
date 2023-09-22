package com.example.lyka_findmeaning

import android.app.Service
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.graphics.PixelFormat
import android.os.Build
import android.os.IBinder
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import android.widget.FrameLayout
import android.widget.RelativeLayout
import androidx.annotation.RequiresApi
import androidx.cardview.widget.CardView

class FloatingWidgetService : Service() {
    private lateinit var flotingview: View
    private lateinit var expandview:View
    private lateinit var collapse:View
    private var windowManager: WindowManager? = null
    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate() {
        super.onCreate()
        flotingview = LayoutInflater.from(this).inflate(R.layout.floating_layout, null)

        windowManager = this.getSystemService(Context.WINDOW_SERVICE) as WindowManager

        val params: WindowManager.LayoutParams = WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY, // Requires SYSTEM_ALERT_WINDOW permission
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            0
        )
        windowManager?.addView(flotingview, params)

        params.x = 0
        params.y = 0

        val parent_view=flotingview.findViewById<RelativeLayout>(R.id.parent_view)
        val parent_cardview=flotingview.findViewById<CardView>(R.id.parent_cardview)

        parent_view.setOnClickListener {
Log.e("#","clicked")
            parent_cardview.radius=0f
           val float_icon_parent=flotingview.findViewById<FrameLayout>(R.id.float_icon_parent)
            float_icon_parent.visibility=View.GONE

            val meaninglayout=flotingview.findViewById<RelativeLayout>(R.id.meaninglayout)
            meaninglayout.visibility=View.VISIBLE




        }
        parent_cardview.setOnTouchListener { view, event ->
            Log.e("#","touch")
            var initialX =params.x
            var initialY = params.y
            var initialTouchX = params.x.toFloat()
            var initialTouchY = params.y.toFloat()



            when (event.getAction()) {
                MotionEvent.ACTION_DOWN -> {
                    initialX = params.x
                    initialY = params.y
                    initialTouchX = event.getRawX();
                    initialTouchY = event.getRawY();
                    return@setOnTouchListener true
                }

                MotionEvent.ACTION_UP -> {//when the drag is ended switching the state of the widget

                    return@setOnTouchListener true
                }

                MotionEvent.ACTION_MOVE -> {//this code is helping the widget to move around the screen with fingers
                    params.x = (initialX + (event.getRawX() - initialTouchX)).toInt();
                    params.y = (initialY + (event.getRawY() - initialTouchY)).toInt();
                    windowManager!!.updateViewLayout(flotingview, params);
                    return@setOnTouchListener true
                }

                else -> {
                    return@setOnTouchListener false
                }
            }
return@setOnTouchListener  false
        }






    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return super.onStartCommand(intent, flags, startId)
        Log.e("#","helllo")
        return START_STICKY
    }
}