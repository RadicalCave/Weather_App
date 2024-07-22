package com.example.weatherapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.databinding.CurrentWeatherRecyclerviewBinding
import com.example.weatherapp.model.Forecast
import com.example.weatherapp.model.WeatherItem

class CurrentWeatherAdapter: RecyclerView.Adapter<CurrentWeatherAdapter.WeatherViewHolder>() {

    var dataset: List<WeatherItem> = emptyList()

    private val diffUtil = object: DiffUtil.ItemCallback<WeatherItem>(){
        override fun areItemsTheSame(oldItem: WeatherItem, newItem: WeatherItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: WeatherItem, newItem: WeatherItem): Boolean {
            return oldItem == newItem
        }
    }

    private val asyncListDiffer = AsyncListDiffer(this, diffUtil)

    fun saveData(weatherItem: List<WeatherItem>){
        asyncListDiffer.submitList(weatherItem)
    }

    class WeatherViewHolder(val binding: CurrentWeatherRecyclerviewBinding)
        :RecyclerView.ViewHolder(binding.root) {
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): WeatherViewHolder {
        val binding = CurrentWeatherRecyclerviewBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return WeatherViewHolder(binding)
    }

    override fun onBindViewHolder(holder: WeatherViewHolder, position: Int) {
        val item = dataset[position]
        with(holder){
//            binding.weatherIcon.drawable = item.icon
            binding.weatherDesc.text = item.description.toString()
        }
    }

    override fun getItemCount() = dataset.size


}