package com.example.weatherapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.model.Forecast
import com.example.weatherapp.repo.WeatherRepo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class WeatherViewModel @Inject constructor(
    private val weatherRepo: WeatherRepo
    ) : ViewModel() {

    private val _uiState = MutableStateFlow<WeatherForecastUiState>(WeatherForecastUiState.Loading)
    val uiState = _uiState.asStateFlow()


    fun getForecast(){
        viewModelScope.launch {
            try {
                val latestForecast = weatherRepo.getWeather()
                _uiState.value = WeatherForecastUiState.Success(latestForecast)
            }catch (e: Throwable){
                _uiState.value = WeatherForecastUiState.Error(
                    e.localizedMessage ?: "Something went wrong")
            }
        }
    }


    sealed class WeatherForecastUiState{
        data class Success(val data: Forecast): WeatherForecastUiState()
        data class Error(val error: String): WeatherForecastUiState()
        object Loading: WeatherForecastUiState()
    }

}