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
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageView
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
    override fun onCreate(){
        super.onCreate()
        flotingview = LayoutInflater.from(this).inflate(R.layout.floating_layout, null)

        windowManager = this.getSystemService(Context.WINDOW_SERVICE) as WindowManager

        val params: WindowManager.LayoutParams = WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY, // Requires SYSTEM_ALERT_WINDOW permission
            0,
            0
        )
        windowManager?.addView(flotingview, params)

        params.x = 0
        params.y = 0

        val parent_view=flotingview.findViewById<RelativeLayout>(R.id.parent_view)
        val parent_cardview=flotingview.findViewById<CardView>(R.id.parent_cardview)
        val closebtn=flotingview.findViewById<ImageView>(R.id.closebtn)
        val float_icon_parent=flotingview.findViewById<FrameLayout>(R.id.float_icon_parent)
        val meaninglayout=flotingview.findViewById<RelativeLayout>(R.id.meaninglayout)
        val inputfield=flotingview.findViewById<EditText>(R.id.inputfield)


//       parent_cardview.setOnTouchListener { view, event ->
//
//            Log.e("#","touch")
//             var initialX=params.x
//            var initialY=params.y
//            var initialTouchX=event.rawX
//            var initialTouchY=event.rawY
//
//
//
//            when (event.getAction()) {
//                MotionEvent.ACTION_DOWN -> {
//                    initialX = params.x
//                    initialY = params.y
//                    initialTouchX = event.getRawX();
//                    initialTouchY = event.getRawY();
//                    return@setOnTouchListener false
//                }
//
//                MotionEvent.ACTION_UP -> {//when the drag is ended switching the state of the widget
//
//                    return@setOnTouchListener false
//                }
//
//                MotionEvent.ACTION_MOVE -> {//this code is helping the widget to move around the screen with fingers
//
//                    Log.e("#","move")
//                    params.x = initialX +  (event.getRawX() - initialTouchX).toInt()
//                    params.y = initialY +  (event.getRawY() - initialTouchY).toInt()
//                    windowManager!!.updateViewLayout(flotingview, params)
//                    return@setOnTouchListener false
//                }
//
//
//            }
//return@setOnTouchListener  false
//
//
//        }

        var initialX = 0
        var initialY = 0
        var initialTouchX = 0f
        var initialTouchY = 0f

        parent_cardview.setOnTouchListener { view, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    // Capture the initial X and Y coordinates
                    initialX = params.x
                    initialY = params.y

                    // Capture the initial touch point
                    initialTouchX = event.rawX
                    initialTouchY = event.rawY

                    return@setOnTouchListener false // Return true to consume the event
                }

                MotionEvent.ACTION_UP -> {
                    // Handle touch up event if needed
                    return@setOnTouchListener false // Return true to consume the event
                }

                MotionEvent.ACTION_MOVE -> {
                    // Calculate the new X and Y positions based on the initial positions
                    params.x = initialX + (event.rawX - initialTouchX).toInt()
                    params.y = initialY + (event.rawY - initialTouchY).toInt()

                    // Update the view's position
                    windowManager!!.updateViewLayout(flotingview, params)

                    return@setOnTouchListener false // Return true to consume the event
                }

                else -> {
                    return@setOnTouchListener false
                }
            }
            return@setOnTouchListener false
        }






        parent_cardview.setOnClickListener {
            Log.e("#","clicked")
            parent_cardview.radius=0f
            float_icon_parent.visibility=View.GONE

            meaninglayout.visibility=View.VISIBLE
            inputfield.requestFocus()

            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(inputfield, InputMethodManager.SHOW_IMPLICIT)



        }
        inputfield.setOnClickListener {
           Log.e("#","edit text")
            it.requestFocus()
            it.isFocusable=true
            it.isFocusableInTouchMode=true

            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(it, InputMethodManager.SHOW_IMPLICIT)


        }

        closebtn.setOnClickListener {
            parent_cardview.radius=100f
            float_icon_parent.visibility=View.VISIBLE

            meaninglayout.visibility=View.GONE
        }




    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return super.onStartCommand(intent, flags, startId)
        Log.e("#","helllo")
        return START_STICKY
    }
}