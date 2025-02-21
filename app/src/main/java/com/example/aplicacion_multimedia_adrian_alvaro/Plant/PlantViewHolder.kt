package com.example.aplicacion_multimedia_adrian_alvaro.Plant

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.aplicacion_multimedia_adrian_alvaro.databinding.ActivityPlantViewBinding

class PlantViewHolder(private val binding: ActivityPlantViewBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(plant: Plant) {
        binding.tvNombre.text = plant.commonName
        binding.tvLatin.text = plant.latinName
        binding.ivPlantImage.setImageResource(plant.imageResId) // Cargar imagen local
    }

    companion object {
        fun from(parent: ViewGroup): PlantViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ActivityPlantViewBinding.inflate(layoutInflater, parent, false)
            return PlantViewHolder(binding)
        }
    }
}
