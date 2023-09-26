package com.example.lyka_findmeaning

import android.app.ActivityManager
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.lyka_findmeaning.databinding.ActivityMainBinding
import com.example.lyka_findmeaning.viewmodel.Getmeaningviewmodel
import dagger.hilt.android.AndroidEntryPoint


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding;
    val SYSTEM_ALERT_WINDOW_PERMISSION_REQUEST_CODE = 101



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        if (!hasSystemAlertWindowPermission()) {
            showPermissiondialog()
        }

        binding.enablebtn.setOnClickListener {
            if (!hasSystemAlertWindowPermission()) {
                showPermissiondialog()

            } else {
                Log.e("#", "lol")
                val intent = Intent(this, FloatingWidgetService::class.java)
                startService(intent)

            }
        }

    }

    private fun hasSystemAlertWindowPermission(): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return Settings.canDrawOverlays(this)
        }
        return true
    }

    private fun askPermission() {
        val intent = Intent(
            Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
            Uri.parse("package:$packageName")
        )

        startActivityForResult(intent, SYSTEM_ALERT_WINDOW_PERMISSION_REQUEST_CODE)
    }

    private fun showPermissiondialog() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setCancelable(true)
        builder.setTitle("Screen Overlay Permission Needed")
        builder.setMessage("Enable 'Display over other apps' from System Settings.")
        builder.setPositiveButton("Open Settings",
            DialogInterface.OnClickListener { _, _ ->

                askPermission()
            })
        builder.create()

        builder.show()
    }


}