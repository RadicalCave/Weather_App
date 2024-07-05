package com.example.weatherapp.api

import com.example.weatherapp.model.Forecast

interface ApiHelper {

    suspend fun getWeather(): Forecast

}