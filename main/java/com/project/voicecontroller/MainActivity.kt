package com.project.voicecontroller
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val button1 = findViewById<Button>(R.id.button1)
        val button2 = findViewById<Button>(R.id.button2)
        val button3 = findViewById<Button>(R.id.button3)
        val button4 = findViewById<Button>(R.id.button4)
        val button5 = findViewById<Button>(R.id.button5)
        val button6 = findViewById<Button>(R.id.button6)
        button1.setOnClickListener {
            // Switch to Layout2Activity
            val intent = Intent(this, BMI::class.java)
            startActivity(intent)
        }
        button2.setOnClickListener {
            // Switch to Layout2Activity
            val intent = Intent(this, Flashlight::class.java)
            startActivity(intent)
        }
        button3.setOnClickListener {
            // Switch to Layout2Activity
            val intent = Intent(this, Taskmain::class.java)
            startActivity(intent)
        }
        button4.setOnClickListener {
            // Switch to Layout2Activity
            val intent = Intent(this, Timer::class.java)
            startActivity(intent)
        }
        button5.setOnClickListener {
            // Switch to Layout2Activity
            val intent = Intent(this, MainActivity2::class.java)
            startActivity(intent)
        }
        button6.setOnClickListener {
            // Switch to Layout2Activity
            val intent = Intent(this, MainActivity3::class.java)
            startActivity(intent)
        }
        }
}



