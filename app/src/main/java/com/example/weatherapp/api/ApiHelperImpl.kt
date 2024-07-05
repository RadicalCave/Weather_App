package com.example.weatherapp.api

import com.example.weatherapp.model.Forecast
import javax.inject.Inject

class ApiHelperImpl @Inject constructor(private val apiService: WeatherAPI): ApiHelper {

    override suspend fun getWeather(): Forecast = apiService.getWeather()

}