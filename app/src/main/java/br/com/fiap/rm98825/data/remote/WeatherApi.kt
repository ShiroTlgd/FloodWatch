package br.com.fiap.rm98825.data.remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {
    @GET("v1/forecast")
    suspend fun getWeather(
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double,
        @Query("current") current: String = "precipitation,rain",
        @Query("hourly") hourly: String = "precipitation_probability,precipitation",
        @Query("forecast_days") forecastDays: Int = 2,
        @Query("timezone") timezone: String = "auto"
    ): WeatherResponse

    companion object {
        fun create(): WeatherApi = Retrofit.Builder()
            .baseUrl("https://api.open-meteo.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(WeatherApi::class.java)
    }
}
