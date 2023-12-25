package com.project.voicecontroller

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class BMI : AppCompatActivity() {

    private var spinnerWeight: Spinner? = null
    private var spinnerHeight: Spinner? = null
    private var editTextWeight: EditText? = null
    private var editTextHeight: EditText? = null
    private var btnCalculate: Button? = null
    private var textViewResult: TextView? = null

    private companion object {
        const val POUNDS = "Pounds"
        const val CENTIMETERS = "Centimeters"
        const val INCHES = "Inches"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bmi)

        spinnerWeight = findViewById(R.id.spinnerWeight)
        spinnerHeight = findViewById(R.id.spinnerHeight)
        editTextWeight = findViewById(R.id.editTextWeight)
        editTextHeight = findViewById(R.id.editTextHeight)
        btnCalculate = findViewById(R.id.btnCalculate)
        textViewResult = findViewById(R.id.textViewResult)

        val weightAdapter = ArrayAdapter.createFromResource(
            this, R.array.weight_units, android.R.layout.simple_spinner_item
        ).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }
        spinnerWeight?.adapter = weightAdapter

        val heightAdapter = ArrayAdapter.createFromResource(
            this, R.array.height_units, android.R.layout.simple_spinner_item
        ).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }
        spinnerHeight?.adapter = heightAdapter

        btnCalculate?.setOnClickListener {
            Toast.makeText(this, "Button clicked", Toast.LENGTH_SHORT).show()
            calculateBMI()
         }

    }

    private fun calculateBMI() {
        val weightText = editTextWeight?.text.toString()
        val heightText = editTextHeight?.text.toString()

        if (weightText.isBlank() || heightText.isBlank()) {
            Toast.makeText(this, "Please enter weight and height", Toast.LENGTH_SHORT).show()
            return
        }

        try {
            var weight = weightText.toDouble()
            var height = heightText.toDouble()

            val selectedWeightUnit = spinnerWeight?.selectedItem.toString()
            if (selectedWeightUnit == POUNDS) {
                weight *= 0.453592 // 1 pound = 0.453592 kilograms
            }

            val selectedHeightUnit = spinnerHeight?.selectedItem.toString()
            if (selectedHeightUnit == CENTIMETERS) {
                height /= 100.0 // Convert centimeters to meters
            } else if (selectedHeightUnit == INCHES) {
                height *= 0.0254 // 1 inch = 0.0254 meters
            }

            val bmi = weight / (height * height)
            val result = "Your BMI: ${"%.2f".format(bmi)}"

            // Set the result in the textViewResult TextView
            if(result<= 18.5.toString())
            {
                println("under waight")
            }
            textViewResult?.text = result
        } catch (e: NumberFormatException) {
            Toast.makeText(this, "Invalid input. Please enter valid numbers", Toast.LENGTH_SHORT).show()
        }
    }



    fun onNextButtonClick(view: View) {val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)}
}
