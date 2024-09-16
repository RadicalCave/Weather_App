package com.example.weatherapp.fragments

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.res.stringResource
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weatherapp.R
import com.example.weatherapp.adapter.CurrentWeatherAdapter
import com.example.weatherapp.databinding.FragmentWeatherBinding
import com.example.weatherapp.viewmodel.WeatherViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.time.LocalTime

@AndroidEntryPoint
class WeatherFragment : Fragment(R.layout.fragment_weather) {

    private lateinit var binding: FragmentWeatherBinding
    private lateinit var itemAdapter: CurrentWeatherAdapter
    private val viewModel: WeatherViewModel by activityViewModels()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentWeatherBinding.bind(view)

        setUpRecyclerView()

        val currentTime: LocalTime = LocalTime.now()
        val dusk = LocalTime.parse("18:00:00")
        val dawn = LocalTime.parse("06:00:00")

        //Set dark mode after 6PM or before 6AM based on system time
        if (currentTime.isAfter(dusk) || currentTime.isBefore(dawn)){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }else{
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }

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

    private fun setUpRecyclerView() = with(binding.currentWeatherRecyclerview){
        layoutManager = LinearLayoutManager(requireContext())
        itemAdapter = CurrentWeatherAdapter()
        adapter = itemAdapter
    }

    private fun uiStateHandle(status: WeatherViewModel.WeatherForecastUiState){
        when(status){
            is WeatherViewModel.WeatherForecastUiState.Success -> {
                bindUI(status)
            }
            is WeatherViewModel.WeatherForecastUiState.Error -> {
                Toast.makeText(requireContext(),status.error,Toast.LENGTH_LONG).show()
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
                Toast.makeText(requireContext(), "Location Found",Toast.LENGTH_LONG).show()
                //Cache recent location coordinates
                viewLifecycleOwner.lifecycleScope.launch {
                    repeatOnLifecycle(Lifecycle.State.STARTED){
                        viewModel.setLatCoords(status.location.lat)
                        viewModel.setLonCoords(status.location.lon)
                    }
                }
            }
            is WeatherViewModel.WeatherLocationUiState.Error -> {
                Toast.makeText(requireContext(),"Enter Valid Location",Toast.LENGTH_LONG).show()
            }
            is WeatherViewModel.WeatherLocationUiState.Loading -> {

            }
        }
    }

    private fun bindUI(status: WeatherViewModel.WeatherForecastUiState.Success){
        with(binding){
            locationInfo.text = getString(R.string.location, status.data.name,status.data.sys?.country)
            temp.text = getString(R.string.celsius, status.data.main?.temp)
            humidity.text = getString(R.string.humidity, status.data.main?.humidity.toString())
            feelsLike.text = getString(R.string.feels_like, status.data.main?.feelsLike)
            tempMin.text = getString(R.string.temp_min, status.data.main?.tempMin)
            tempMax.text = getString(R.string.temp_max, status.data.main?.tempMax)
            rainLvl.text = getString(R.string.rain_lvl, status.data.rain?.jsonMember1h ?: 0.0)
            windSpd.text = getString(R.string.wind_spd, status.data.wind?.speed)
            itemAdapter.dataset = status.data.weather?.mapNotNull { it } ?: emptyList()
            itemAdapter.saveData(status.data.weather)
        }
    }

}