package com.example.weatherapp.repo

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.doublePreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject

class DataStorePreferencesRepoImpl @Inject constructor(
    private val coordsDataStorePreferences: DataStore<Preferences>
): DataStorePreferencesRepo {


    override suspend fun getLat(): Result<Double> {
        return Result.runCatching {
            val flow = coordsDataStorePreferences.data
                .catch { exception ->
                    if (exception is IOException){
                        emit(emptyPreferences())
                    }else{
                        throw exception
                    }
                }.map {preferences ->
                    preferences[LAT_COORDS]
                }
            val value = flow.firstOrNull() ?: 0.0
            value
        }
    }

    override suspend fun getLon(): Result<Double> {
        return Result.runCatching {
            val flow = coordsDataStorePreferences.data
                .catch { exception ->
                    if (exception is IOException){
                        emit(emptyPreferences())
                    }else{
                        throw exception
                    }
                }.map {preferences ->
                    preferences[LON_COORDS]
                }
            val value = flow.firstOrNull() ?: 0.0
            value
        }
    }

    override suspend fun setLat(lat: Double) {
        Result.runCatching {
            coordsDataStorePreferences.edit {preferences ->
                preferences[LAT_COORDS] = lat
            }
        }
    }

    override suspend fun setLon(lon: Double) {
        Result.runCatching {
            coordsDataStorePreferences.edit {preferences ->
                preferences[LON_COORDS] = lon
            }
        }
    }

    private companion object{
        val LAT_COORDS = doublePreferencesKey(
            name = "lat"
        )
        val LON_COORDS = doublePreferencesKey(
            name = "lon"
        )
    }

}