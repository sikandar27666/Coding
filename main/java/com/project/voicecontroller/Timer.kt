// Timer.kt

package com.project.voicecontroller

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.os.SystemClock
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import java.util.concurrent.TimeUnit

class Timer : AppCompatActivity() {

    private lateinit var editTextMinutes: EditText
    private lateinit var chronometerTimer: Chronometer
    private lateinit var btnStart: Button
    private lateinit var btnStop: Button
    private lateinit var textViewTimer: TextView
    private lateinit var notificationSoundSpinner: Spinner
    private lateinit var countDownTimer: CountDownTimer
    private var timeRemainingMillis: Long = 0
    private var isTimerRunning = false

    private lateinit var notificationManager: NotificationManager

    companion object {
        private const val NOTIFICATION_CHANNEL_ID = "CountdownTimerChannel"
        private const val NOTIFICATION_ID = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_timer)

        editTextMinutes = findViewById(R.id.editTextMinutes)
        chronometerTimer = findViewById(R.id.chronometerTimer)
        btnStart = findViewById(R.id.btnStart)
        btnStop = findViewById(R.id.btnStop)
        textViewTimer = findViewById(R.id.textViewTimer)
        notificationSoundSpinner = findViewById(R.id.notificationSoundSpinner)

        setupNotificationSoundSpinner()

        btnStart.setOnClickListener {
            if (!isTimerRunning) {
                val minutes = editTextMinutes.text.toString().toLongOrNull() ?: 0
                val selectedSound = getSelectedSoundUri()
                startTimer(minutes, selectedSound)
            }
        }

        btnStop.setOnClickListener {
            stopTimer()
        }

        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }

    fun onNextButtonClick(view: View) {
        // Create an Intent to navigate to the next activity/layout
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    private fun setupNotificationSoundSpinner() {
        val soundResources = arrayOf(
            RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION),
            RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM),
            RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE)
        )

        val soundNames = arrayOf("Default Notification Sound", "Default Alarm Sound", "Default Ringtone")

        val soundAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, soundNames)
        soundAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        notificationSoundSpinner.adapter = soundAdapter
        notificationSoundSpinner.setSelection(0) // Default selection

        notificationSoundSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parentView: AdapterView<*>?, selectedItemView: View?, position: Int, id: Long) {
                // Handle sound selection if needed
            }

            override fun onNothingSelected(parentView: AdapterView<*>?) {
                // Do nothing here
            }
        }
    }

    private fun getSelectedSoundUri(): Uri {
        val soundResources = arrayOf(
            RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION),
            RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM),
            RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE)
        )

        val selectedItemPosition = notificationSoundSpinner.selectedItemPosition
        return soundResources.getOrNull(selectedItemPosition) ?: Uri.EMPTY
    }

    private fun startTimer(minutes: Long, selectedSound: Uri) {
        if (minutes <= 0) {
            Toast.makeText(this, "Please enter a valid timer duration.", Toast.LENGTH_SHORT).show()
            return
        }

        timeRemainingMillis = TimeUnit.MINUTES.toMillis(minutes)

        countDownTimer = object : CountDownTimer(timeRemainingMillis, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                timeRemainingMillis = millisUntilFinished
                updateTimerText()
            }

            override fun onFinish() {
                isTimerRunning = false
                chronometerTimer.stop()
                textViewTimer.text = "Timer Finished!"
                showNotification("Countdown Timer", "Your timer has finished.", selectedSound)
            }
        }

        countDownTimer.start()
        isTimerRunning = true
        chronometerTimer.base = SystemClock.elapsedRealtime() + timeRemainingMillis
        chronometerTimer.start()

        // Disable or change the appearance of UI elements as needed
        editTextMinutes.isEnabled = false
        btnStart.isEnabled = false
    }

    private fun stopTimer() {
        if (isTimerRunning) {
            countDownTimer.cancel()
            isTimerRunning = false
            chronometerTimer.stop()
            updateTimerText()

            // Enable or restore the appearance of UI elements
            editTextMinutes.isEnabled = true
            btnStart.isEnabled = true
        }
    }



    private fun updateTimerText() {
        val minutes = TimeUnit.MILLISECONDS.toMinutes(timeRemainingMillis)
        val seconds = TimeUnit.MILLISECONDS.toSeconds(timeRemainingMillis) % 60
        val timerText = String.format("%02d:%02d", minutes, seconds)
        textViewTimer.text = timerText
    }

    private fun showNotification(title: String, message: String, soundUri: Uri) {
        // Create a Notification Channel (required for Android 8.0 and above)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                "Countdown Timer Channel",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }

        // Create a notification
        val builder = NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setSound(soundUri)

        notificationManager.notify(NOTIFICATION_ID, builder.build())
    }
}
