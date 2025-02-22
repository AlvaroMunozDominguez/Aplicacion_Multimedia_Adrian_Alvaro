package com.example.aplicacion_multimedia_adrian_alvaro.Plant

import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.aplicacion_multimedia_adrian_alvaro.R
import com.example.aplicacion_multimedia_adrian_alvaro.databinding.ActivityPlantViewBinding
import java.io.File

class PlantViewHolder(private val binding: ActivityPlantViewBinding) :
    RecyclerView.ViewHolder(binding.root) {


    fun bind(plant: Plant) {
        binding.tvNombre.text = plant.nombre
        binding.tvLatin.text = plant.latin

        // Verificar si la URL de la imagen no es nula ni vacía
        if (!plant.imageUrl.isNullOrEmpty()) {
            val imgFile = File(plant.imageUrl)

            if (imgFile.exists()) {
                binding.ivPlantImage.setImageURI(Uri.fromFile(imgFile))
            } else {
                binding.ivPlantImage.setImageResource(R.drawable.olivo) // Imagen por defecto si no se encuentra
            }
        } else {
            binding.ivPlantImage.setImageResource(R.drawable.olivo) // Imagen por defecto si no hay ruta
        }
    }

    /*fun bind(plant: Plant) {
        Log.d("ImageLoading", "Cargando imagen de la planta: ${plant.imageUrl}")
        if (!plant.imageUrl.isNullOrEmpty()) {
            val imgFile = File(plant.imageUrl)
            if (imgFile.exists()) {
                binding.ivPlantImage.setImageURI(Uri.fromFile(imgFile))
            } else {
                binding.ivPlantImage.setImageResource(R.drawable.olivo)
            }
        } else {
            binding.ivPlantImage.setImageResource(R.drawable.olivo)
        }
    }*/

    companion object {
        fun from(parent: ViewGroup): PlantViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ActivityPlantViewBinding.inflate(layoutInflater, parent, false)
            return PlantViewHolder(binding)
        }
    }
}
