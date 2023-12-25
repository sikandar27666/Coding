package com.project.voicecontroller

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class ButtonLayoutActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val layoutId = intent.getIntExtra("layoutId", R.layout.activity_main)
        setContentView(layoutId)
    }
}
