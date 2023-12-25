package com.project.voicecontroller

import com.google.gson.annotations.SerializedName

data class ExchangeRateResponse(
    @SerializedName("data")
    val data: Map<String, Double>? = null
)
