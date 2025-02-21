package com.example.aplicacion_multimedia_adrian_alvaro.Plant

data class Plant(
    val id: Int,
    val nombre: String,
    val latin: String,
    val clima: String,
    val pais: String,
    val edad: Int,
    val cantidadRiego: String,
    val imageUrl: String?,
    val extraInfo: String
)
