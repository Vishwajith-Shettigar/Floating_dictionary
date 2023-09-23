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
    private lateinit var expandview: View
    private lateinit var collapse: View
    private var windowManager: WindowManager? = null
    var params: WindowManager.LayoutParams? = null
    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate() {
        super.onCreate()
        flotingview = LayoutInflater.from(this).inflate(R.layout.floating_layout, null)
        windowManager = this.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        params = WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY, // Requires SYSTEM_ALERT_WINDOW permission
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,     // Make it non-focusable
            PixelFormat.TRANSLUCENT
        )
        windowManager?.addView(flotingview, params)

        params!!.x = 0
        params!!.y = 0

        val parent_view = flotingview.findViewById<RelativeLayout>(R.id.parent_view)
        val parent_cardview = flotingview.findViewById<CardView>(R.id.parent_cardview)
        val closebtn = flotingview.findViewById<ImageView>(R.id.closebtn)
        val float_icon_parent = flotingview.findViewById<FrameLayout>(R.id.float_icon_parent)
        val meaninglayout = flotingview.findViewById<RelativeLayout>(R.id.meaninglayout)
        val inputfield = flotingview.findViewById<EditText>(R.id.inputfield)
        val powerbtn=flotingview.findViewById<ImageView>(R.id.powerbtn)
        val homebtn=flotingview.findViewById<ImageView>(R.id.homebtn)


        inputfield.setOnClickListener {
            makeWidgetFocusable()
            it.isFocusable = true
            it.isFocusableInTouchMode = true
            it.requestFocus()
            val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(inputfield, InputMethodManager.SHOW_IMPLICIT)

        }

        var initialX = 0
        var initialY = 0
        var initialTouchX = 0f
        var initialTouchY = 0f

        parent_cardview.setOnTouchListener { view, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    // Capture the initial X and Y coordinates
                    initialX = params!!.x
                    initialY = params!!.y

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
                    params!!.x = initialX + (event.rawX - initialTouchX).toInt()
                    params!!.y = initialY + (event.rawY - initialTouchY).toInt()

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
            Log.e("#", "clicked")
            parent_cardview.radius = 15f
            float_icon_parent.visibility = View.GONE
            meaninglayout.visibility = View.VISIBLE
            makeWidgetNotFocusable()
        }

        closebtn.setOnClickListener {
            parent_cardview.radius = 100f
            float_icon_parent.visibility = View.VISIBLE
            meaninglayout.visibility = View.GONE
            makeWidgetNotFocusable()
        }

        powerbtn.setOnClickListener {
            Log.e("#","stop service")
            val stopServiceIntent = Intent(this, FloatingWidgetService::class.java)
            stopService(stopServiceIntent)
        }

        homebtn.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
            startActivity(intent)
        }

    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return super.onStartCommand(intent, flags, startId)
        Log.e("#", "helllo")
        return START_STICKY
    }

    fun makeWidgetFocusable() {
        val x=params!!.x
        val y=params!!.y
        params = WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY, // Requires SYSTEM_ALERT_WINDOW permission
            0,     // Make it non-focusable
            PixelFormat.TRANSLUCENT
        )
        params!!.x=x
        params!!.y=y
        windowManager!!.updateViewLayout(flotingview, params)
    }

    fun makeWidgetNotFocusable() {
        val x=params!!.x
        val y=params!!.y
        params = WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.TRANSLUCENT
        )
        params!!.x=x
        params!!.y=y
        windowManager!!.updateViewLayout(flotingview, params)
    }

    override fun onDestroy() {
        super.onDestroy()
        if (flotingview != null) windowManager!!.removeView(flotingview);
    }
}