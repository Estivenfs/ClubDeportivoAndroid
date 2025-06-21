package com.deportes.clubdeportivo.models

data class Actividad(
    val id_actividad: Int,
    val nombre_actividad: String,
    val precio_actividad: Double,
    val cupo_actividad: Int,
    val inscriptos: Int
)
