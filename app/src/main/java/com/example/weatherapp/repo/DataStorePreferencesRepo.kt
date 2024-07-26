package com.example.weatherapp.repo

interface DataStorePreferencesRepo {

    suspend fun getLat(): Result<Double>

    suspend fun getLon(): Result<Double>

    suspend fun setLat(lat: Double)

    suspend fun setLon(lon: Double)

}