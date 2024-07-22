package com.example.weatherapp.api

import com.example.weatherapp.model.Forecast
import com.example.weatherapp.model.GeoCode
import com.example.weatherapp.model.GeoCodeItem
import javax.inject.Inject

class ApiHelperImpl @Inject constructor(private val apiService: WeatherAPI): ApiHelper {

    override suspend fun getCurrentWeather(lat: Double?, lon: Double?): Forecast = apiService.getCurrentWeather(lat, lon)

    override suspend fun getLocation(cityName: String): GeoCodeItem = apiService.getLocation(cityName)


}