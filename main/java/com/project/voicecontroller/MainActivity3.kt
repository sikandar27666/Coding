package com.project.voicecontroller

import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity3 : AppCompatActivity() {

    private lateinit var amountEditText: EditText
    private lateinit var sourceCurrencySpinner: Spinner
    private lateinit var targetCurrencySpinner: Spinner
    private lateinit var convertButton: Button
    private lateinit var resultTextView: TextView

    private val apiKey = "fca_live_p57dkaPvl6xHzzOj4LOi9OqYLYweseGeRRhaRfHB" // Replace with your actual API key

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://api.freecurrencyapi.com/v1/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val currencyApi = retrofit.create(CurrencyApi::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main3)

        amountEditText = findViewById(R.id.amountEditText)
        sourceCurrencySpinner = findViewById(R.id.sourceCurrencySpinner)
        targetCurrencySpinner = findViewById(R.id.targetCurrencySpinner)
        convertButton = findViewById(R.id.convertButton)
        resultTextView = findViewById(R.id.resultTextView)

        val currencies = listOf(
            "AUD", "BGN", "BRL", "CAD", "CHF", "CNY", "CZK", "DKK", "EUR", "GBP",
            "HKD", "HRK", "HUF", "IDR", "ILS", "INR", "ISK", "JPY", "KRW", "MXN",
            "MYR", "NOK", "NZD", "PHP", "PLN", "RON", "RUB", "SEK", "SGD", "THB", "TRY",
            "USD", "ZAR"
        )

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, currencies)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        sourceCurrencySpinner.adapter = adapter
        targetCurrencySpinner.adapter = adapter

        convertButton.setOnClickListener {
            convertCurrency()
        }
    }

    private fun convertCurrency() {
        val amount = amountEditText.text.toString().toDoubleOrNull() ?: return
        val sourceCurrency = sourceCurrencySpinner.selectedItem.toString()
        val targetCurrency = targetCurrencySpinner.selectedItem.toString()

        val call = currencyApi.getExchangeRates(apiKey, sourceCurrency)
        call.enqueue(object : Callback<ExchangeRateResponse> {
            override fun onResponse(call: Call<ExchangeRateResponse>, response: Response<ExchangeRateResponse>) {
                if (response.isSuccessful) {
                    val rateResponse = response.body()
                    Log.d("CurrencyConverter", "API Response: $rateResponse")

                    if (rateResponse != null) {
                        val exchangeRate = rateResponse.data?.get(targetCurrency)
                        if (exchangeRate != null) {
                            val result = convertCurrency(amount, exchangeRate)
                            runOnUiThread {
                                resultTextView.text = "$amount $sourceCurrency = $result $targetCurrency"
                            }
                        } else {
                            Log.e("CurrencyConverter", "Target currency '$targetCurrency' not found in rates. Available rates: ${rateResponse.data?.keys}")
                            showConversionError("Target currency not found in rates.")
                        }
                    } else {
                        Log.e("CurrencyConverter", "Empty response body.")
                        showConversionError("Empty response body.")
                    }
                } else {
                    Log.e("CurrencyConverter", "Failed to fetch exchange rates. Code: ${response.code()}")
                    showConversionError("Failed to fetch exchange rates. Code: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<ExchangeRateResponse>, t: Throwable) {
                Log.e("CurrencyConverter", "Conversion failed", t)
                showConversionError("Conversion failed. Check your internet connection.")
            }
        })
    }

    private fun convertCurrency(amount: Double, exchangeRate: Double): Double {
        return amount * exchangeRate
    }

    private fun showConversionError(message: String) {
        runOnUiThread {
            resultTextView.text = "Conversion failed. $message"
        }
    }
}
