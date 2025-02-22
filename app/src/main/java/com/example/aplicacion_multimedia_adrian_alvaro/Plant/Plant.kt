package com.example.aplicacion_multimedia_adrian_alvaro.Plant

data class Plant(
    val id: Int = 0,
    val nombre: String = "",
    val latin: String = "",
    val clima: String = "",
    val pais: String = "",
    val edad: Int = 0,
    val cantidadRiego: String = "",
    val imageResId: Int = 0,
    val imageUrl: String? = null,
    val extraInfo: String = ""
)
