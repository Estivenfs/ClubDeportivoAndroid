package com.deportes.clubdeportivo

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import android.content.Intent

class PagosBusquedaActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pagos_busqueda)

        val btnBuscar = findViewById<Button>(R.id.btnBuscar)
        btnBuscar.setOnClickListener {
            // Lógica para manejar el clic en el botón "Buscar"
            // Cambiar a activity correspondiente
            startActivity(Intent(this, PagosDetallesSocioActivity::class.java))
        }

    }
}