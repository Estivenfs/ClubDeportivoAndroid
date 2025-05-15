package com.deportes.clubdeportivo

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.content.Intent
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView

class PagosBusquedaActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_pagos_busqueda)

        val btnBuscar = findViewById<Button>(R.id.btnBuscar)
        btnBuscar.setOnClickListener {
            // Lógica para manejar el clic en el botón "Buscar"
            // Cambiar a activity correspondiente
            startActivity(Intent(this, PagosDetallesSocioActivity::class.java))
        }

        // Importación y lógica de la barra superior
        val btnAtras: ImageView = findViewById(R.id.buttonBack)
        val textViewTitulo: TextView = findViewById(R.id.textViewTitle)
        textViewTitulo.text = "Pagos"

        btnAtras.setOnClickListener {
            finish()
        }

        // Importacion y lógica de barra inferior
        val btnMenu = findViewById<LinearLayout>(R.id.btnMenu)
        val btnConsultas = findViewById<LinearLayout>(R.id.btnConsultas)
        val btnPagos = findViewById<LinearLayout>(R.id.btnPagos)
        val btnClientes = findViewById<LinearLayout>(R.id.btnNuevoCliente)

        // Asignar este valor a la pantalla en la que se encuentre
        btnPagos.alpha = 0.5f // Establece la transparencia al 50%
        btnPagos.isEnabled = false // Opcionalmente, desactiva los clics

        btnMenu.setOnClickListener {
            startActivity(Intent(this, MenuPrincipalActivity::class.java))
        }

        btnConsultas.setOnClickListener {
            startActivity(Intent(this, ConsultasActivity::class.java))
        }

        btnPagos.setOnClickListener {
        // Opción deshabilitada para esta sección
        // startActivity(Intent(this, PagosBusquedaActivity::class.java))
        }

        btnClientes.setOnClickListener {
            startActivity(Intent(this, NuevoClienteActivity::class.java))
        }

    }
}