package com.deportes.clubdeportivo

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import android.view.View

class PagosDetallesSocioActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pagos_detalles_socio)


        // Lógica de la barra superior
        val btnAtras: ImageView = findViewById(R.id.buttonBack)
        val textViewTitulo: TextView = findViewById(R.id.textViewTitle)

        textViewTitulo.text = "Detalles de Pago (Socio)"

        btnAtras.setOnClickListener {
            finish()
        }

        val inputFechaPago = findViewById<TextView>(R.id.inputFechaPago)
        val inputFechaInicio = findViewById<TextView>(R.id.inputFechaInicio)
        val fechaPagoLayout = findViewById<LinearLayout>(R.id.fechaPagoLayout)
        val fechaInicioLayout = findViewById<LinearLayout>(R.id.fechaInicioLayout)
        val btnCalcular = findViewById<Button>(R.id.btnCalcularPagoSocio)

        fechaPagoLayout.setOnClickListener {
            DatePickerUtils.mostrarDatePickerDialog(this, inputFechaPago) // Pasar el contexto de la actividad
        }
        fechaInicioLayout.setOnClickListener {
            DatePickerUtils.mostrarDatePickerDialog(this, inputFechaInicio) // Pasar el contexto de la actividad
        }

        // Configuración del botón Calcular

        btnCalcular.setOnClickListener {
            startActivity(Intent(this, DetallePagoActivity::class.java))
        }
    }

}