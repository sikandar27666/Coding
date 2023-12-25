package com.project.voicecontroller

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface CurrencyApi {
    @GET("latest")
    fun getExchangeRates(
        @Query("apikey") apiKey: String,
        @Query("base_currency") baseCurrency: String
    ): Call<ExchangeRateResponse>
}
