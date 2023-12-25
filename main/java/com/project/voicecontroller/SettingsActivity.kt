package com.project.voicecontroller
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.project.voicecontroller.MainActivity2
import java.io.BufferedReader
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.InputStreamReader

class SettingsActivity : AppCompatActivity(), OnSeekBarChangeListener {
    private var seekBar: SeekBar? = null
    private var textView: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        seekBar = findViewById<View>(R.id.skbThreshold) as SeekBar
        textView = findViewById<View>(R.id.tvThresholdValue) as TextView

        // Load settings from file
        val progress: String = loadSettingsFromFile()
        seekBar!!.progress = progress.toInt()
        textView!!.text = progress

        // Set SeekBar change listener
        seekBar!!.setOnSeekBarChangeListener(this)

        // Load image using Glide
        val imageView = findViewById<View>(R.id.ivShakeGif) as ImageView
        Glide.with(applicationContext)
            .load(R.drawable.chopflashlight)
            .into(imageView)
    }

    fun saveSettings(view: View?) {
        // Save settings to file
        if (saveSettingsToFile(seekBar!!.progress.toString())) {
            Log.i("Flashlight_saveSettings", "File Saved Successfully")
            startActivity(Intent(this, MainActivity2::class.java))
            finish()
        } else {
            Log.e("Flashlight_saveSettings", "Error while saving file")
        }
    }

    override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
        textView!!.text = StringBuilder().append(progress)
    }

    override fun onStartTrackingTouch(seekBar: SeekBar) {
        // Unused method
    }

    override fun onStopTrackingTouch(seekBar: SeekBar) {
        // Unused method
    }

    private fun saveSettingsToFile(data: String): Boolean {
        return try {
            val file = File(applicationContext.filesDir, "settings.txt")
            val fos = FileOutputStream(file)
            fos.write(data.toByteArray())
            fos.close()
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    private fun loadSettingsFromFile(): String {
        return try {
            val file = File(applicationContext.filesDir, "settings.txt")
            val fis = FileInputStream(file)
            val isr = InputStreamReader(fis)
            val br = BufferedReader(isr)
            val sb = StringBuilder()
            var line: String?

            while (br.readLine().also { line = it } != null) {
                sb.append(line)
            }

            br.close()
            isr.close()
            fis.close()

            sb.toString()
        } catch (e: Exception) {
            e.printStackTrace()
            "0" // Default value if there's an error
        }
    }
}
