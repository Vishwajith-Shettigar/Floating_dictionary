package com.example.lyka_findmeaning

import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.PixelFormat
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.cardview.widget.CardView
import androidx.constraintlayout.utils.widget.MotionButton
import androidx.lifecycle.LifecycleCoroutineScope
import com.example.lyka_findmeaning.data.word
import com.example.lyka_findmeaning.repository.Getmeaningrepository
import com.example.lyka_findmeaning.util.Resource
import com.github.leandroborgesferreira.loadingbutton.customViews.CircularProgressButton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class FloatingWidgetService : Service() {


    private lateinit var flotingview: View
    private lateinit var expandview: View
    private lateinit var collapse: View
    private var windowManager: WindowManager? = null
    var params: WindowManager.LayoutParams? = null
    private val _meaning = MutableStateFlow<Resource<word>>(Resource.Notspecified())
    val meaning = _meaning.asStateFlow()

   lateinit var parent_view:View
   lateinit var parent_cardview :View
  lateinit  var float_icon_parent :View
   lateinit var meaninglayout:View
  lateinit  var inputfield:EditText
  lateinit  var powerbtn :View
  lateinit  var homebtn :View
   lateinit var searchbtn: CircularProgressButton
   lateinit var closebtn:View
   lateinit var tv_meaning:TextView

    private val respository by lazy {
        Getmeaningrepository()
    }

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
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.TRANSLUCENT
        )
        windowManager?.addView(flotingview, params)

        params!!.x = 0
        params!!.y = 0

        parent_cardview = flotingview.findViewById<CardView>(R.id.parent_cardview)
        closebtn = flotingview.findViewById<ImageView>(R.id.closebtn)
        float_icon_parent = flotingview.findViewById<FrameLayout>(R.id.float_icon_parent)
        parent_view = flotingview.findViewById<RelativeLayout>(R.id.parent_view)
        meaninglayout = flotingview.findViewById<RelativeLayout>(R.id.meaninglayout)
        inputfield = flotingview.findViewById<EditText>(R.id.inputfield)
        powerbtn = flotingview.findViewById<ImageView>(R.id.powerbtn)
        homebtn = flotingview.findViewById<ImageView>(R.id.homebtn)
        searchbtn = flotingview.findViewById<CircularProgressButton>(R.id.search)
        tv_meaning = flotingview.findViewById<TextView>(R.id.tv_meaning)


emptyText()

        inputfield.setOnClickListener {
            makeWidgetFocusable()
            it.isFocusable = true
            it.isFocusableInTouchMode = true
            it.requestFocus()
            val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(inputfield, InputMethodManager.SHOW_IMPLICIT)

        }

        GlobalScope.launch {
            meaning.collectLatest {
                when (it) {
                    is Resource.Loading -> {
                        emptyText()
                        showLoading()
                    }
                    is Resource.Success -> {
                        stopLoading()
                        showMeaning(it.data)
                    }
                    is Resource.Error -> {
                        stopLoading()
                        emptyText()
                        showError()

                    }
                    else -> {
                        Unit
                    }
                }
            }
        }

        searchbtn.setOnClickListener {

            GlobalScope.launch {
                _meaning.emit(Resource.Loading())

            }
            val word = (inputfield as EditText?)?.text.toString().trim()
            Log.e("#", word)
            GlobalScope.launch(Dispatchers.IO) {
                val res = respository.getMeaning(word, object : Getmeaningcallback {
                    override fun onSuccess(word: word?) {

                        GlobalScope.launch {
                            _meaning.emit(Resource.Success(word!!))
                        }
                        Log.e(
                            "#",
                            word?.meanings?.get(0)?.definitions?.get(0)?.definition.toString()
                        )
                    }

                    override fun onError(errorMessage: String) {
                        TODO("Not yet implemented")
                        GlobalScope.launch {
                            _meaning.emit(Resource.Error(errorMessage))

                        }
                    }

                })


            }


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
            (parent_cardview as CardView?)?.radius  = 15f
            float_icon_parent.visibility = View.GONE
            meaninglayout.visibility = View.VISIBLE
            makeWidgetNotFocusable()
        }

        closebtn.setOnClickListener {
            (parent_cardview as CardView?)?.radius= 100f
            float_icon_parent.visibility = View.VISIBLE
            meaninglayout.visibility = View.GONE
            makeWidgetNotFocusable()
        }

        powerbtn.setOnClickListener {
            Log.e("#", "stop service")
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

    private fun emptyText() {
        val mainHandler = Handler(Looper.getMainLooper())
        mainHandler.post {
            tv_meaning.visibility = View.GONE
        }
    }

    private fun showError() {

    }

    private fun showMeaning(data: word?) {
        val mainHandler = Handler(Looper.getMainLooper())
        mainHandler.post {
            tv_meaning.visibility = View.VISIBLE
            val input = data?.meanings?.get(0)?.definitions?.get(0)?.definition + "\n" +
                    data?.meanings?.get(0)?.definitions?.get(0)?.example
            tv_meaning.text = (input.toString())
        }

    }

    private fun stopLoading() {


        val mainHandler = Handler(Looper.getMainLooper())
        mainHandler.post {
            searchbtn.revertAnimation()
        }
    }

    private fun showLoading() {


        val mainHandler = Handler(Looper.getMainLooper())
        mainHandler.post {
            searchbtn.startAnimation()
        }



    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return super.onStartCommand(intent, flags, startId)
        Log.e("#", "helllo")
        return START_STICKY
    }

    fun makeWidgetFocusable() {
        val x = params!!.x
        val y = params!!.y
        params = WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY, // Requires SYSTEM_ALERT_WINDOW permission
            0,     // Make it non-focusable
            PixelFormat.TRANSLUCENT
        )
        params!!.x = x
        params!!.y = y
        windowManager!!.updateViewLayout(flotingview, params)
    }

    fun makeWidgetNotFocusable() {
        val x = params!!.x
        val y = params!!.y
        params = WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.TRANSLUCENT
        )
        params!!.x = x
        params!!.y = y
        windowManager!!.updateViewLayout(flotingview, params)
    }

    override fun onDestroy() {
        super.onDestroy()
        if (flotingview != null) windowManager!!.removeView(flotingview);
    }
}