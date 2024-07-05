package com.example.weatherapp.api

import com.example.weatherapp.model.Forecast
import retrofit2.http.GET

interface WeatherAPI {

    @GET("data/2.5/forecast?id=524901&appid={13aa9ff47cf52d00b5ec71483a1f29b3}")
    suspend fun getWeather(): Forecast

}