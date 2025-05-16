package com.deportes.clubdeportivo

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import android.widget.ImageView
import android.widget.TextView

class PagosBusquedaActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pagos_busqueda)

        // Lógica de la barra superior
        val btnAtras: ImageView = findViewById(R.id.buttonBack)
        val textViewTitulo: TextView = findViewById(R.id.textViewTitle)

        textViewTitulo.text = "Busqueda de Pagos"

        btnAtras.setOnClickListener {
            finish()
        }

        val btnBuscar = findViewById<Button>(R.id.btnBuscar)
        btnBuscar.setOnClickListener {
            // Lógica para manejar el clic en el botón "Buscar"
            // Cambiar a activity correspondiente
            startActivity(Intent(this, PagosDetallesSocioActivity::class.java))
        }

    }
}