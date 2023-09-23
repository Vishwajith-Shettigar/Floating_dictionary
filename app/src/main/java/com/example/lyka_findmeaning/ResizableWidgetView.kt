package com.example.lyka_findmeaning

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.widget.RelativeLayout

class ResizableWidgetView(context: Context, attrs: AttributeSet) : RelativeLayout(context, attrs) {

    private var initialX = 0f
    private var initialY = 0f
    private var initialWidth = 0
    private var initialHeight = 0

    init {
        // Initialize your custom view here
        // Add any necessary UI elements and layout
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val x = event.rawX
        val y = event.rawY

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                initialX = x
                initialY = y
                initialWidth = width
                initialHeight = height
            }
            MotionEvent.ACTION_MOVE -> {
                val deltaX = x - initialX
                val deltaY = y - initialY

                layoutParams.width = (initialWidth + deltaX).toInt()
                layoutParams.height = (initialHeight + deltaY).toInt()
                requestLayout()
            }
        }
        return true
    }
}
