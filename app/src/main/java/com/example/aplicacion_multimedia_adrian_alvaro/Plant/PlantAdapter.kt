package com.example.aplicacion_multimedia_adrian_alvaro.Plant

import android.util.Log
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView


class PlantAdapter(private var plantList: List<Plant>, private val onClick: (Plant) -> Unit) :
    RecyclerView.Adapter<PlantViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlantViewHolder {
        return PlantViewHolder.from(parent)

        /*val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ActivityPlantViewBinding.inflate(layoutInflater, parent, false)
        return PlantViewHolder(binding)*/
    }

    override fun onBindViewHolder(holder: PlantViewHolder, position: Int) {
        holder.bind(plantList[position])
        holder.itemView.setOnClickListener { onClick(plantList[position]) }
    }

    //override fun getItemCount(): Int = plantList.size
    override fun getItemCount(): Int {
        Log.d("ADAPTER", "Elementos en Adapter: ${plantList.size}")  //Verifica cuántos elementos hay, debugging.
        return plantList.size
    }

    //Función para actualizar la lista sin necesidad de crear un nuevo adapter
    fun updateList(newList: List<Plant>) {
        plantList = newList
        notifyDataSetChanged()  //Notificar cambios en los datos
        Log.d("ADAPTER", "Lista actualizada con ${plantList.size} elementos")  //Debugging
    }
}
