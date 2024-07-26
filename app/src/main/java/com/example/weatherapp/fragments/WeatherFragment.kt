package com.example.weatherapp.fragments

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.weatherapp.R
import com.example.weatherapp.databinding.FragmentWeatherBinding
import com.example.weatherapp.viewmodel.WeatherViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
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

        //On app startup, if location is already cached, fetch weather data
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                val latCoords = viewModel.getLatCoords().getOrNull()
                val lonCoords = viewModel.getLonCoords().getOrNull()

                if (latCoords != null && lonCoords != null){
                    viewModel.getForecast(latCoords,lonCoords)
                }
            }
        }

        //Launch two observers simultaneously in single coroutine
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                launch {
                    viewModel.uiState.collect{
                        uiStateHandle(it) }
                }
                launch {
                    viewModel.currentLocation.collect{
                        locationStateHandle(it) }
                }
            }
        }


        //Enter city name and get weather
        binding.getWeatherBtn.setOnClickListener {
            val cityName: String = binding.searchCity.editText?.text.toString().trim()
            viewLifecycleOwner.lifecycleScope.launch {
                repeatOnLifecycle(Lifecycle.State.STARTED){
                    viewModel.getLocation(cityName)
//                    viewModel.getForecast(lat = 16.7967129, lon = 96.1609916)
                }
            }
        }

    }


    private fun uiStateHandle(status: WeatherViewModel.WeatherForecastUiState){
        when(status){
            is WeatherViewModel.WeatherForecastUiState.Success -> {
                binding.currentLocation.text = status.data.name
                binding.temp.text = getString(R.string.celsius, status.data.main?.temp.toString())
                binding.humidity.text = getString(R.string.humidity, status.data.main?.humidity.toString())
                binding.feelsLike.text = getString(R.string.feels_like, status.data.main?.feelsLike.toString())
            }
            is WeatherViewModel.WeatherForecastUiState.Error -> {
                print(status.error)
            }
            is WeatherViewModel.WeatherForecastUiState.Loading -> {
                //display loading
            }
        }
    }

    private fun locationStateHandle(status: WeatherViewModel.WeatherLocationUiState){
        when(status){
            is WeatherViewModel.WeatherLocationUiState.LocationFound -> {
                binding.currentLocation.text = status.location.name
                Toast.makeText(requireContext(), "getForecast",Toast.LENGTH_LONG).show()
                viewLifecycleOwner.lifecycleScope.launch {
                    repeatOnLifecycle(Lifecycle.State.STARTED){
                        viewModel.getForecast(status.location.lat,status.location.lon)}}
                        //Cache location
//                        viewModel.setLatCoords(status.location.lat)
//                        viewModel.setLonCoords(status.location.lon)
                    }
            is WeatherViewModel.WeatherLocationUiState.Error -> {
                print(status.error)
            }
            is WeatherViewModel.WeatherLocationUiState.Loading -> {

            }
        }
    }
}