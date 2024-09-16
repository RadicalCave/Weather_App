package com.example.weatherapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.model.Forecast
import com.example.weatherapp.model.GeoCodeItem
import com.example.weatherapp.repo.DataStorePreferencesRepo
import com.example.weatherapp.repo.WeatherRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val weatherRepo: WeatherRepo,
    private val coordsDataStorePreferencesRepo: DataStorePreferencesRepo
    ) : ViewModel() {

    private val _uiState = MutableStateFlow<WeatherForecastUiState>(WeatherForecastUiState.Loading)
    val uiState = _uiState.asStateFlow()

    private val _currentLocation = MutableStateFlow<WeatherLocationUiState>(WeatherLocationUiState.Loading)
    val currentLocation = _currentLocation.asStateFlow()


    fun getLocation(cityName: String) {
        viewModelScope.launch {
            try {
                val forecastLocation = weatherRepo.getLocation(cityName)
                for (data in forecastLocation.indices){
                    val jsonObject = forecastLocation[data]
                    _currentLocation.value = WeatherLocationUiState.LocationFound(jsonObject)
                    val lat = jsonObject.lat
                    val lon = jsonObject.lon
                    getForecast(lat,lon)
                }
            } catch (e: Throwable) {
                _currentLocation.value = WeatherLocationUiState.Error(
                    e.localizedMessage ?: "Invalid Location"
                )
            }
        }
    }

    fun getForecast(lat: Double?, lon: Double?) {
        viewModelScope.launch {
            try {
                val latestForecast = weatherRepo.getCurrentWeather(lat, lon)
                _uiState.value = WeatherForecastUiState.Success(latestForecast)
            } catch (e: Throwable) {
                _uiState.value = WeatherForecastUiState.Error(
                    e.localizedMessage ?: "Something went wrong"
                )
            }
        }
    }

    sealed class WeatherForecastUiState {
        data class Success(val data: Forecast) : WeatherForecastUiState()
        data class Error(val error: String) : WeatherForecastUiState()
        object Loading : WeatherForecastUiState()
    }

    sealed class WeatherLocationUiState {
        data class LocationFound(val location: GeoCodeItem) : WeatherLocationUiState()
        data class Error(val error: String) : WeatherLocationUiState()
        object Loading : WeatherLocationUiState()
    }

    suspend fun getLatCoords(): Result<Double> {
        return coordsDataStorePreferencesRepo.getLat()
    }

    suspend fun setLatCoords(latCoords: Double?) {
        if (latCoords != null) {
            coordsDataStorePreferencesRepo.setLat(latCoords)
        }
    }

    suspend fun getLonCoords(): Result<Double> {
        return coordsDataStorePreferencesRepo.getLon()
    }

    suspend fun setLonCoords(lonCoords: Double?) {
        if (lonCoords != null) {
            coordsDataStorePreferencesRepo.setLon(lonCoords)
        }
    }

}