package com.example.weatherapp.api

import com.example.weatherapp.model.Forecast
import com.example.weatherapp.model.GeoCode
import com.example.weatherapp.model.GeoCodeItem

interface ApiHelper {

    suspend fun getCurrentWeather(lat: Double?, lon: Double?): Forecast

    suspend fun getLocation(cityName: String): GeoCodeItem

}