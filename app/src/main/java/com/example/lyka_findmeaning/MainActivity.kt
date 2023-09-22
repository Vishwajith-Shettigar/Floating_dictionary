package com.example.lyka_findmeaning

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.lyka_findmeaning.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    private lateinit var binding:ActivityMainBinding;
    val SYSTEM_ALERT_WINDOW_PERMISSION_REQUEST_CODE=101

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)




        binding.enablebtn.setOnClickListener {
            if(!hasSystemAlertWindowPermission()){
                askPermission()

            }
            else{
                Log.e("#","lol")
                val intent=Intent(this,FloatingWidgetService::class.java)
                startService(intent)

            }
        }


    }
    private fun hasSystemAlertWindowPermission(): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return Settings.canDrawOverlays(this)
        }
        return true  // For versions prior to Android 6.0, the permission is granted by default.
    }
    private fun askPermission() {
        val intent = Intent(
            Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
            Uri.parse("package:$packageName")
        )

        startActivityForResult(intent, SYSTEM_ALERT_WINDOW_PERMISSION_REQUEST_CODE)
    }


}