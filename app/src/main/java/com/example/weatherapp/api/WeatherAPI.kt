package com.example.weatherapp.api

import com.example.weatherapp.BuildConfig
import com.example.weatherapp.model.Forecast
import com.example.weatherapp.model.GeoCode
import com.example.weatherapp.model.GeoCodeItem
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherAPI {

    private companion object{
        private const val apiKey = BuildConfig.WEATHER_API_KEY
    }

    //Get Current Weather Data
    @GET("data/2.5/weather")
    suspend fun getCurrentWeather(@Query("lat") lat: Double?,
                                  @Query("lon") lon: Double?,
                                  @Query("appid") appid: String = apiKey,
                                  @Query("units") units: String = "metric"): Forecast

    //Get Coordinates by location name
    @GET("geo/1.0/direct")
    suspend fun getLocation(@Query("q") q: String,
                            @Query("limit") limit: String = "5",
                            @Query("appid") appkey: String = apiKey): GeoCodeItem

}