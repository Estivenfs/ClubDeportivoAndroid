package com.deportes.clubdeportivo.models

class Cliente(
    val idCliente: Int? = null, // INTEGER PRIMARY KEY AUTOINCREMENT
    val nombre: String?,         // TEXT NOT NULL
    val apellido: String?,       // TEXT NOT NULL
    val dni: String?,            // TEXT NOT NULL UNIQUE
    val email: String?,         // TEXT (nullable en SQL, por eso String?)
    val telefono: String?,      // TEXT (nullable en SQL, por eso String?)
    val fechaNacimiento: String?, // DATE (asumimos que se guarda como "yyyy-MM-dd" String)
    val condSocio: Boolean?,     // BOOLEAN NOT NULL
    val aptoFisico: Boolean?     // BOOLEAN NOT NULL
)