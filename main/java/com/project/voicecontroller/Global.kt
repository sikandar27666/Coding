package com.project.voicecontroller

import android.content.Context
import android.hardware.camera2.CameraAccessException
import android.hardware.camera2.CameraManager
import android.os.Vibrator
import android.util.Log
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.io.OutputStreamWriter

@Suppress("DEPRECATION")
class Global {

    companion object {
        private const val VIBRATION_DURATION = 500

        fun torchToggle(command: String, context: Context): Boolean {
            val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
            val camManager = context.getSystemService(Context.CAMERA_SERVICE) as? CameraManager

            return camManager?.let {
                try {
                    val cameraId = it.cameraIdList[0]
                    if (command == "on") {
                        it.setTorchMode(cameraId, true) // Turn ON
                        vibrator?.vibrate(VIBRATION_DURATION.toLong())
                        true
                    } else {
                        it.setTorchMode(cameraId, false) // Turn OFF
                        vibrator?.vibrate(VIBRATION_DURATION.toLong())
                        false
                    }
                } catch (e: CameraAccessException) {
                    Log.e("Global", "CameraAccessException: ${e.message}")
                    false
                }
            } ?: false
        }

        fun saveFile(context: Context, fileName: String?, data: String?): Boolean {
            return try {
                val fos = context.openFileOutput(fileName, Context.MODE_PRIVATE)
                OutputStreamWriter(fos).use {
                    it.write(data)
                }
                Log.i("Global", "File saved successfully")
                true
            } catch (ioEx: IOException) {
                Log.e("Global", "Error saving file: ${ioEx.message}")
                false
            }
        }

        fun loadFile(context: Context, fileName: String?): String? {
            return try {
                context.openFileInput(fileName).use { fileInputStream ->
                    BufferedReader(InputStreamReader(fileInputStream)).use { bufferedReader ->
                        bufferedReader.readLine()
                    }
                }
            } catch (ioEx: IOException) {
                Log.e("Global", "Error loading file: ${ioEx.message}")
                null
            }
        }
    }
}
