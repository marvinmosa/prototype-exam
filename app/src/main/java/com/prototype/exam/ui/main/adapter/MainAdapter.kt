package com.prototype.exam.ui.main.adapter

import android.content.res.Resources
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.prototype.exam.R
import com.prototype.exam.data.model.forecast.ForecastItem
import com.prototype.exam.databinding.ItemLayoutBinding
import com.prototype.exam.ui.main.adapter.MainAdapter.DataViewHolder
import java.text.DecimalFormat


class MainAdapter(private val users: ArrayList<ForecastItem>, private val listener: OnItemClickListener) : RecyclerView.Adapter<DataViewHolder>() {
    inner class DataViewHolder(private val binding: ItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root), View.OnClickListener {
        fun bind(forecast: ForecastItem) {
            itemView.apply {
                val formatter = DecimalFormat("0.0")
                val temperature = formatter.format(forecast.main.temperature.toDouble())

                val color = getColor(temperature.toDouble(), resources)
                binding.container.setBackgroundColor(color)

                val resId = if(forecast.favorite)R.drawable.ic_favorite else 0
                binding.iconFavorite.setImageResource(resId)

                binding.textTemperature.text = resources.getString(R.string.unit_celsius, temperature.toString())
                binding.textLocation.text = forecast.name
                binding.textWeather.text = forecast.weatherList[0].weather
            }
        }

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val position = bindingAdapterPosition
            if (position != RecyclerView.NO_POSITION) {
                listener.onItemClick(position)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder {
        val binding = ItemLayoutBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return DataViewHolder(binding)
    }

    override fun getItemCount(): Int = users.size

    internal fun getItem(position: Int): ForecastItem {
        return users[position]
    }

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        holder.bind(users[position])
    }

    fun addUsers(users: List<ForecastItem>) {
        this.users.apply {
            clear()
            addAll(users)
        }
    }

    private fun getColor(temperature: Double, resources: Resources): Int = when {
        temperature < 0 -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                resources.getColor(R.color.freezing, null)
            } else {
                resources.getColor(R.color.freezing)
            }
        }
        temperature >= 0 && temperature < 15 -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                resources.getColor(R.color.cold, null)
            } else {
                resources.getColor(R.color.cold)
            }
        }
        temperature >= 15 && temperature < 30 -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                resources.getColor(R.color.warm, null)
            } else {
                resources.getColor(R.color.warm)
            }
        }
        else -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                resources.getColor(R.color.hot, null)
            } else {
                resources.getColor(R.color.hot)
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }
}