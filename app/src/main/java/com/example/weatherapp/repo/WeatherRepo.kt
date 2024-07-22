package com.example.weatherapp.repo

import com.example.weatherapp.api.ApiHelper
import javax.inject.Inject

class WeatherRepo @Inject constructor(private val apiHelper: ApiHelper) {

    suspend fun getCurrentWeather(lat: Double?, lon: Double?) = apiHelper.getCurrentWeather(lat, lon)

    suspend fun getLocation(cityName: String) = apiHelper.getLocation(cityName)

}