package com.example.weatherapp.repo

import com.example.weatherapp.api.ApiHelper
import javax.inject.Inject

class WeatherRepo @Inject constructor(private val apiHelper: ApiHelper) {

    suspend fun getWeather() = apiHelper.getWeather()

}