package com.example.aplicacion_multimedia_adrian_alvaro.Plant

import android.util.Log
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView


class PlantAdapter(private val plantList: List<Plant>, private val onClick: (Plant) -> Unit) :
    RecyclerView.Adapter<PlantViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlantViewHolder {
        return PlantViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: PlantViewHolder, position: Int) {
        holder.bind(plantList[position])
        holder.itemView.setOnClickListener { onClick(plantList[position]) }
    }

    //override fun getItemCount(): Int = plantList.size
    override fun getItemCount(): Int {
        Log.d("ADAPTER", "Elementos en Adapter: ${plantList.size}")  // 🔍 Verifica cuántos elementos hay
        return plantList.size
    }
}
