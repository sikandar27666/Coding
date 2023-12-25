package com.project.voicecontroller

import android.content.Intent
import android.hardware.camera2.CameraAccessException
import android.hardware.camera2.CameraManager
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity2 : AppCompatActivity() {

    private var isTorchOn = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
        startService(Intent(this, ShakeDetectService::class.java))
    }

    fun toggle(view: View) {
        val button = view as Button
        if (button.text == "Switch On") {
            button.setText(R.string.switch_off_text)
            toggleFlashlight("on")
        } else {
            button.setText(R.string.switch_on_text)
            toggleFlashlight("off")
        }
    }

    private fun toggleFlashlight(command: String) {
        val camManager = getSystemService(CAMERA_SERVICE) as CameraManager
        try {
            val cameraId = camManager.cameraIdList[0]
            isTorchOn = if (command == "on") {
                camManager.setTorchMode(cameraId, true) // Turn ON
                true
            } else {
                camManager.setTorchMode(cameraId, false) // Turn OFF
                false
            }
        } catch (e: CameraAccessException) {
            // Handle the exception, log or show a message to the user
            e.printStackTrace()
        }
    }

    fun goToSettings(view: View?) {
        startActivity(Intent(this, SettingsActivity::class.java))
    }
}
