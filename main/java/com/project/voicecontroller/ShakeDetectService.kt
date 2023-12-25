package com.project.voicecontroller

import android.app.Service
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Handler
import android.os.IBinder
import android.os.Vibrator
import android.util.Log
import kotlin.math.pow
import kotlin.math.sqrt

class ShakeDetectService : Service(), SensorEventListener {

    private var sensorManager: SensorManager? = null
    private var vibrator: Vibrator? = null
    private var lastShakeTime: Long = 0
    private var isFlashlightOn = false
    private var shakeThreshold: Float = 10.2f

    override fun onCreate() {
        super.onCreate()
        vibrator = getSystemService(VIBRATOR_SERVICE) as Vibrator
        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager

        sensorManager?.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)?.let { accelerometer ->
            sensorManager?.registerListener(
                this,
                accelerometer,
                SensorManager.SENSOR_DELAY_NORMAL
            )
        }

        try {
            shakeThreshold =
                Global.loadFile(applicationContext, "settings.txt")?.toFloat() ?: 10.2f
        } catch (ex: Exception) {
            Log.e("ShakeDetectService", "Error loading shake threshold: ${ex.message}")
            Global.saveFile(applicationContext, "settings.txt", 10.2f.toString())
        }
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onSensorChanged(event: SensorEvent) {
        if (event.sensor.type == Sensor.TYPE_ACCELEROMETER) {
            val curTime = System.currentTimeMillis()
            if (curTime - lastShakeTime > MIN_TIME_BETWEEN_SHAKES) {
                val x = event.values[0]
                val y = event.values[1]
                val z = event.values[2]
                val acceleration =
                    sqrt(x.toDouble().pow(2.0) + y.toDouble().pow(2.0) + z.toDouble().pow(2.0)) -
                            SensorManager.GRAVITY_EARTH

                if (acceleration > shakeThreshold) {
                    lastShakeTime = curTime
                    isFlashlightOn = !isFlashlightOn
                    Global.torchToggle(if (isFlashlightOn) "on" else "off", this)
                }
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {
        // Unused Method
    }

    companion object {
        const val MIN_TIME_BETWEEN_SHAKES = 1000
    }
}
