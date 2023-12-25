package com.project.voicecontroller
import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.hardware.camera2.CameraAccessException
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Button
import android.widget.Toast
import android.widget.ToggleButton
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class Flashlight : AppCompatActivity() {
    private var cameraManager: CameraManager? = null
    private var cameraId: String? = null
    private var isFlashlightOn = false
    private var isBlinking = false
    private var handler: Handler? = null
    private val CAMERA_PERMISSION_REQUEST = 123

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_flashlight)

        // Check if the device has a flashlight
        if (!packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)) {
            // Display an error message or handle the lack of flashlight feature
            finish()
        }

        // Request CAMERA permission at runtime
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.CAMERA),
                CAMERA_PERMISSION_REQUEST
            )
        } else {
            initializeFlashlight()
        }
    }

    // Handle permission request result
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            CAMERA_PERMISSION_REQUEST -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission granted, initialize flashlight
                    initializeFlashlight()
                } else {
                    // Permission denied, display a message or take appropriate action
                    Toast.makeText(
                        this,
                        "Camera permission is required for the flashlight.",
                        Toast.LENGTH_SHORT
                    ).show()
                    finish()
                }
            }
        }
    }

    // Initialize flashlight after obtaining necessary permissions
    private fun initializeFlashlight() {
        // Initialize cameraManager and cameraId
        cameraManager = getSystemService(CAMERA_SERVICE) as CameraManager
        cameraId = findCameraWithFlash()

        // Initialize UI elements
        val toggleButtonFlashlight = findViewById<ToggleButton>(R.id.toggleButtonFlashlight)
        val buttonBlink = findViewById<Button>(R.id.buttonBlink)

        // Set onClickListener for toggleButtonFlashlight
        toggleButtonFlashlight.setOnClickListener { toggleFlashlight() }

        // Set onClickListener for buttonBlink
        buttonBlink.setOnClickListener { toggleBlinking() }

        // Initialize handler for blinking functionality
        handler = Handler(Looper.getMainLooper())
    }

    // Method called when the "Next" button is clicked
    fun onNextButtonClick(view: View) {
        // Create an Intent to navigate to the next activity/layout
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    private fun toggleFlashlight() {
        try {
            isFlashlightOn = !isFlashlightOn
            cameraManager!!.setTorchMode(cameraId!!, isFlashlightOn)
        } catch (e: CameraAccessException) {
            e.printStackTrace()
        }
    }

    private fun toggleBlinking() {
        if (!isBlinking) {
            startBlinking()
        } else {
            stopBlinking()
        }
    }

    private fun startBlinking() {
        handler!!.post(blinkRunnable)
        isBlinking = true
    }

    private fun stopBlinking() {
        handler!!.removeCallbacks(blinkRunnable)
        toggleFlashlight() // Ensure flashlight is turned off when blinking stops
        isBlinking = false
    }

    private fun findCameraWithFlash(): String? {
        val cameraManager = getSystemService(CAMERA_SERVICE) as CameraManager
        val cameraIds = cameraManager.cameraIdList

        for (cameraId in cameraIds) {
            val characteristics = cameraManager.getCameraCharacteristics(cameraId)
            val flashAvailable = characteristics.get(CameraCharacteristics.FLASH_INFO_AVAILABLE)

            if (flashAvailable == true) {
                return cameraId
            }
        }

        return null
    }

    private val blinkRunnable: Runnable = object : Runnable {
        override fun run() {
            toggleFlashlight()
            handler!!.postDelayed(this, 500) // Adjust the blink interval here (e.g., 500ms)
        }
    }
}
