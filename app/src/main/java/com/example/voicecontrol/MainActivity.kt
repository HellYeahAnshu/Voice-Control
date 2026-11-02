package com.example.voicecontrol

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {

    private lateinit var status: TextView
    private lateinit var log: TextView

    private val permissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { result ->
            if (result[Manifest.permission.RECORD_AUDIO] == true) startListener()
            else appendLog("Microphone permission denied")
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        status = findViewById(R.id.status)
        log = findViewById(R.id.log)

        findViewById<Button>(R.id.btnAccessibility).setOnClickListener {
            startActivity(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS))
        }

        findViewById<Button>(R.id.btnStartService).setOnClickListener {
            val perms = mutableListOf(Manifest.permission.RECORD_AUDIO)
            if (Build.VERSION.SDK_INT >= 33) perms += Manifest.permission.POST_NOTIFICATIONS
            val missing = perms.filter {
                ContextCompat.checkSelfPermission(this, it) != PackageManager.PERMISSION_GRANTED
            }
            if (missing.isEmpty()) startListener() else permissionLauncher.launch(missing.toTypedArray())
        }

        val input = findViewById<EditText>(R.id.commandInput)
        findViewById<Button>(R.id.btnRun).setOnClickListener {
            val cmd = input.text.toString()
            Toast.makeText(this, "Switch to the target app now (3s)...", Toast.LENGTH_SHORT).show()
            // Delay so you can leave this screen; otherwise the command would act on this app itself.
            Handler(Looper.getMainLooper()).postDelayed({
                appendLog("$cmd -> ${CommandExecutor.execute(cmd)}")
            }, 3000)
        }
    }

    override fun onResume() {
        super.onResume()
        val enabled = VoiceAccessibilityService.instance != null
        status.text = "Accessibility service: " + if (enabled) "ENABLED" else "not enabled"
    }

    private fun startListener() {
        ContextCompat.startForegroundService(this, Intent(this, ListenerService::class.java))
        appendLog("Listener service started")
    }

    private fun appendLog(line: String) {
        log.text = "$line\n${log.text}"
    }
}
