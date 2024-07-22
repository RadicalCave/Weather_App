package com.example.weatherapp.fragments

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.util.Log.v
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.view.MenuCompat
import androidx.core.view.MenuProvider
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.R
import com.example.weatherapp.databinding.FragmentWeatherBinding
import com.example.weatherapp.viewmodel.WeatherForecastUiState
import com.example.weatherapp.viewmodel.WeatherLocationUiState
import com.example.weatherapp.viewmodel.WeatherViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class WeatherFragment : Fragment(R.layout.fragment_weather) {

    private lateinit var binding: FragmentWeatherBinding
    private val viewModel: WeatherViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentWeatherBinding.bind(view)

        binding.composeView.apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                //Composable
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.uiState.collect{
                    uiStateHandle(it)
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.currentLocation.collect{
                    locationStateHandle(it)
                }
            }
        }



        binding.getWeatherBtn.setOnClickListener {
            val cityName: String = binding.searchCity.editText?.text.toString().trim()
            viewLifecycleOwner.lifecycleScope.launch {
                repeatOnLifecycle(Lifecycle.State.STARTED){
                    viewModel.getForecast(lat = 16.7967129, lon = 96.1609916)
                }
            }
        }

    }



    private fun uiStateHandle(status: WeatherForecastUiState){
        when(status){
            is WeatherForecastUiState.Success -> {
                binding.currentLocation.text = status.data.name
                binding.temp.text = getString(R.string.celsius, status.data.main?.temp.toString())
                binding.humidity.text = getString(R.string.humidity, status.data.main?.humidity.toString())
                binding.feelsLike.text = getString(R.string.feels_like, status.data.main?.feelsLike.toString())
            }
            is WeatherForecastUiState.Error -> {
                print(status.error)
            }
            is WeatherForecastUiState.Loading -> {
                //display loading
            }
        }
    }

    private fun locationStateHandle(status: WeatherLocationUiState){
        when(status){
            is WeatherLocationUiState.Success -> {
                binding.currentLocation.text = status.location.name
                Toast.makeText(requireContext(), "getForecast",Toast.LENGTH_LONG).show()
                viewModel.viewModelScope.launch {
                    repeatOnLifecycle(Lifecycle.State.STARTED){
                        viewModel.getForecast(status.location.lat,status.location.lon)
                    }
                }
            }
            is WeatherLocationUiState.Error -> {
                print(status.error)
            }
            is WeatherLocationUiState.Loading -> {

            }
        }
    }
}