package com.example.weatherapp.fragments

import android.os.Bundle
import android.util.Log.v
import androidx.fragment.app.Fragment
import android.view.View
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.databinding.FragmentWeatherBinding
import com.example.weatherapp.viewmodel.WeatherViewModel
import kotlinx.coroutines.launch

class WeatherFragment : Fragment() {

    lateinit var binding: FragmentWeatherBinding
    private lateinit var viewModel: WeatherViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentWeatherBinding.bind(view)

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.uiState.collect{
                    UiStateHandle(it)
                }
            }
        }
    }

    private fun UiStateHandle(status: WeatherViewModel.WeatherForecastUiState){
        when(status){
            is WeatherViewModel.WeatherForecastUiState.Success -> {
                //display
            }
            is WeatherViewModel.WeatherForecastUiState.Error -> {
                print(status.error)
            }
            is WeatherViewModel.WeatherForecastUiState.Loading -> {
                //display loading
            }
        }
    }
}