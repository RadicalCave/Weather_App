package com.example.weatherapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.databinding.CurrentWeatherRecyclerviewBinding
import com.example.weatherapp.model.WeatherItem
import com.squareup.picasso.Picasso

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

    fun saveData(weatherItem: List<WeatherItem?>?){
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
            Picasso.get().load("${iconUrl}${item.icon}${imgSpec}")
                .resize(500,500)
                .into(binding.weatherIcon)
            binding.weatherDesc.text = item.description.toString().uppercase()
        }
    }

    override fun getItemCount() = dataset.size

    private companion object{
        val iconUrl = "https://openweathermap.org/img/wn/"
        val imgSpec = "@2x.png"
    }

}