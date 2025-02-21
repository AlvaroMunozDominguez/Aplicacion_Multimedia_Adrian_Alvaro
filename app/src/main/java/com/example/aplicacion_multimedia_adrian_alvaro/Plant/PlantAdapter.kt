package com.example.aplicacion_multimedia_adrian_alvaro.Plant

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class PlantAdapter(private val plantList: List<Plant>) :
    RecyclerView.Adapter<PlantViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlantViewHolder {
        return PlantViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: PlantViewHolder, position: Int) {
        holder.bind(plantList[position])
    }

    override fun getItemCount(): Int = plantList.size
}
