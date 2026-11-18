package br.com.fiap.rm98825.data.remote

import com.google.gson.annotations.SerializedName

data class WeatherResponse(
    val current: CurrentDto?,
    val hourly: HourlyDto?
)

data class CurrentDto(
    val time: String?,
    val precipitation: Double?,
    val rain: Double?
)

data class HourlyDto(
    val time: List<String>?,
    @SerializedName("precipitation_probability") val precipitationProbability: List<Int?>?,
    val precipitation: List<Double?>?
)
